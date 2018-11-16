package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.permission.IPermissionManager
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import kotlinx.android.synthetic.main.third_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Third Page Fragment
 */
class ThirdPageFragment: AbstractPageFragment<AppTutorialComponent>(),
    IPermissionManager.OnCheckPermissionListener {

    private lateinit var appTutorialHandler: IAppTutorialHandler

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.third_page_fragment_layout

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IAppTutorialHandler)
            throw IllegalStateException("The context should implement IAppTutorialHandler")

        appTutorialHandler = context
    }

    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("When Phase Is Hidden")

        if(currentPosition > pagePosition) {
            if(locationHistorySwitch?.isOn == false || callsHistorySwitch?.isOn == false
                    || contactsListSwitch?.isOn == false || textMessageSwitch?.isOn == false) {

                appTutorialHandler.showNoticeDialog(R.string.third_page_review_permission_request, object : NoticeDialogFragment.NoticeDialogListener {
                    override fun onAccepted(dialog: DialogFragment) {
                        appTutorialHandler.requestFocus()
                    }
                })
            }
        }


    }

    /**
     * Whe Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("When Phase Is Showed")
    }

    /**
     * Permission Manager
     */
    @Inject
    internal lateinit var permissionManager: IPermissionManager

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): AppTutorialComponent? {
        val appTutorialComponent = AppTutorialComponent::class.java
        .cast((activity as HasComponent<AppTutorialComponent>)
                .component)
        appTutorialComponent.inject(this)
        return appTutorialComponent
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionManager.setCheckPermissionListener(this)

        // Location History Switch Handler

        locationHistorySwitch.isOn =
                !permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        locationHistorySwitch.isEnabled =
                permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        locationHistorySwitch.setOnToggledListener { toggleableView, isOn ->

            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionManager.checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        getString(R.string.third_page_location_history_reason))
            }

        }

        // Calls History

        callsHistorySwitch.isOn = !permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)
        callsHistorySwitch.isEnabled = permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)

        callsHistorySwitch.setOnToggledListener{toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_CALL_LOG,
                        getString(R.string.third_page_call_logs_reason))
            }
        }

        // Contacts List

        contactsListSwitch.isOn = !permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)
        contactsListSwitch.isEnabled = permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)

        contactsListSwitch.setOnToggledListener{ toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_CONTACTS,
                        getString(R.string.third_page_contacts_reason))
            }
        }


        // Text Message Switch

        textMessageSwitch.isOn =
                !permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)

        textMessageSwitch.isEnabled =
                permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)

        textMessageSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_SMS,
                        getString(R.string.third_page_text_messages_reason))
            }
        }

    }

    /**
     * Change Switch Status
     */
    private fun changeSwitchStatus(permission: String, status: Boolean) {
        when(permission) {

            Manifest.permission.ACCESS_FINE_LOCATION -> {
                locationHistorySwitch.isOn = status
                locationHistorySwitch.isEnabled = !status
            }

            Manifest.permission.READ_CONTACTS -> {
                contactsListSwitch.isOn = status
                contactsListSwitch.isEnabled = !status
            }

            Manifest.permission.READ_CALL_LOG -> {
                callsHistorySwitch.isOn = status
                callsHistorySwitch.isEnabled = !status
            }

            Manifest.permission.READ_SMS -> {
                textMessageSwitch.isOn = status
                textMessageSwitch.isEnabled = !status
            }
        }
    }

    /**
     * On Single Permission Granted
     */
    override fun onSinglePermissionGranted(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, true)
    }

    /**
     * On Single Permission Rejected
     */
    override fun onSinglePermissionRejected(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, false)
    }

    /**
     * On Error Occurred
     */
    override fun onErrorOccurred(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, false)
    }

    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.locationHistory, Direction.RIGHT_TO_LEFT, 0.07f),
                TransformItem.create(R.id.callsHistory, Direction.LEFT_TO_RIGHT, 0.07f),
                TransformItem.create(R.id.contactsList, Direction.RIGHT_TO_LEFT, 0.07f),
                TransformItem.create(R.id.textMessage, Direction.LEFT_TO_RIGHT, 0.07f)
        )
    }
}