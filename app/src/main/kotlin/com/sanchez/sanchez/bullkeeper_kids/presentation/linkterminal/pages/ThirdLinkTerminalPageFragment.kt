package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.jaredrummler.android.device.DeviceName
import com.sanchez.sanchez.bullkeeper_kids.BuildConfig
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.lang.IllegalStateException
import kotlinx.android.synthetic.main.third_link_terminal_page_fragment_layout.*
import javax.inject.Inject
import android.telephony.TelephonyManager
import com.sanchez.sanchez.bullkeeper_kids.core.extension.invisible
import com.sanchez.sanchez.bullkeeper_kids.core.extension.visible


/**
 * Third Link Terminal Page Fragment
 */
class ThirdLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Dependencies
     * ==================
     */

    /**
     * Third Link Terminal View Model
     */
    @Inject
    lateinit var thirdLinkTerminalViewModel: ThirdLinkTerminalViewModel

    /**
     * Picasso
     */
    @Inject
    lateinit var picasso: Picasso

    /**
     * App Context
     */
    @Inject
    lateinit var appContext: Context

    /**
     * Preference Repository
     */
    @Inject
    lateinit var preferenceRepository: IPreferenceRepository

    private val tm: TelephonyManager by lazy {
        appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.third_link_terminal_page_fragment_layout

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): LinkDeviceTutorialComponent {
        val linkDeviceTutorialComponent =
                LinkDeviceTutorialComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        linkDeviceTutorialComponent?.inject(this)
        return linkDeviceTutorialComponent
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        /**
         * Check Context
         */
        if(context !is ILinkDeviceTutorialHandler)
            throw IllegalStateException("The context does not implement the handler ILinkDeviceTutorialHandler")

        linkDeviceTutorialHandler = context

    }

    /**
     * On View Create
     */
    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deviceInfoObserver = Observer<DeviceName.DeviceInfo>{
            deviceNameTextView.text = "${it?.manufacturer} - ${it?.name}"
        }

        thirdLinkTerminalViewModel.deviceInfo.observe(this, deviceInfoObserver)

        val terminalSavedObserver = Observer<TerminalEntity> {
            linkDeviceTutorialHandler.hideProgressDialog()
            linkDeviceTutorialHandler.showNoticeDialog(R.string.terminal_successfully_linked, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    linkDeviceTutorialHandler.releaseFocus()
                }
            })
        }

        thirdLinkTerminalViewModel.terminalSaved.observe(this, terminalSavedObserver)

        val errorSaveTerminalObserver = Observer<Failure> {
            linkDeviceTutorialHandler.hideProgressDialog()
            linkDeviceTutorialHandler.showNoticeDialog(R.string.terminal_linked_failed)
        }

        thirdLinkTerminalViewModel.errorSaveTerminal.observe(this, errorSaveTerminalObserver)

        val noTerminalLinkedObserver = Observer<Failure> {
            linkDeviceTutorialHandler.hideProgressDialog()
            linkTerminal.isEnabled = true
            if(isSimSupport()){
                networkOperatorNameTextView.visible()
                tfnoInputLayout.visible()
                networkOperatorNameTextView.text = tm.networkOperatorName
                tfnoInput.requestFocus()
            } else {
                networkOperatorNameTextView.invisible()
                tfnoInputLayout.invisible()
            }
        }

        thirdLinkTerminalViewModel.noTerminalLinked.observe(this, noTerminalLinkedObserver)

    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")

        if(currentPosition > pagePosition
                && preferenceRepository.getPrefTerminalIdentity()
                        == IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {
            linkDeviceTutorialHandler.showNoticeDialog(R.string.confirm_linking_device_required, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    linkDeviceTutorialHandler.requestFocus()
                }
            })
        }
    }

    /**
     * When Phase Is Showed
     */
    @SuppressLint("SetTextI18n", "HardwareIds", "MissingPermission")
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")

        if(linkDeviceTutorialHandler.hasCurrentKidEntity()) {

            thirdLinkTerminalViewModel.getDeviceInformation()


            val currentKidEntity = linkDeviceTutorialHandler.getCurrentKidEntity()

            // Set Kid Full Name
            kidFullNameTextView.text = "${currentKidEntity?.firstName} ${currentKidEntity?.lastName}"

            // Set School Name
            schoolNameTextView.text = currentKidEntity?.school?.name

            picasso.load(currentKidEntity?.profileImage)
                    .placeholder(R.drawable.kid_default)
                    .error(R.drawable.kid_default)
                    .into(childImageImageView)

            linkTerminal.setOnClickListener {

                val isValid = isSimSupport() && !tfnoInput.text.isNullOrBlank()
                        || !isSimSupport()


                if (isValid) {

                    tfnoInputLayout.error = null

                    linkDeviceTutorialHandler.showProgressDialog(R.string.link_device_in_progress)

                    val deviceInfo = thirdLinkTerminalViewModel.deviceInfo.value;

                    val deviceId = Settings.Secure.getString(appContext.contentResolver,
                            Settings.Secure.ANDROID_ID)

                    // Get System TELEPHONY service reference
                    val tManager =
                            appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                    // Save Terminal
                    thirdLinkTerminalViewModel.saveTerminal(
                            kidId = currentKidEntity?.identity!!,
                            appVersionName = BuildConfig.VERSION_NAME,
                            appVersionCode = BuildConfig.VERSION_CODE.toString(),
                            manufacturer = deviceInfo?.manufacturer!!,
                            marketName = deviceInfo.marketName,
                            codeName = deviceInfo.codename,
                            name = deviceInfo.name,
                            deviceName = deviceInfo.name,
                            deviceId = deviceId,
                            model = deviceInfo.model,
                            osVersion = Build.VERSION.RELEASE,
                            sdkVersion = Build.VERSION.SDK_INT.toString(),
                            carrierName = tManager.networkOperatorName,
                            phoneNumber = tfnoInput.text.toString()

                    )

                } else {
                    tfnoInputLayout.error = getString(R.string.terminal_phone_number_required)
                }
            }

            currentKidEntity?.identity?.let {
                // Check Terminal Status
                linkDeviceTutorialHandler.showProgressDialog(R.string.generic_loading_text)
                thirdLinkTerminalViewModel.checkTerminalStatus(it)
            }

        }

    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT,
                        0.2f),
                TransformItem.create(R.id.childImageImageView, Direction.RIGHT_TO_LEFT,
                        0.7f),
                TransformItem.create(R.id.kidFullNameTextView, Direction.LEFT_TO_RIGHT,
                        0.7f),
                TransformItem.create(R.id.schoolNameTextView, Direction.LEFT_TO_RIGHT,
                        0.7f),
                TransformItem.create(R.id.descriptionText, Direction.RIGHT_TO_LEFT,
                        0.7f)

        )
    }

    /**
     * is Sim Support
     */
    private fun isSimSupport(): Boolean {
        return tm.simState != TelephonyManager.SIM_STATE_ABSENT
    }

}