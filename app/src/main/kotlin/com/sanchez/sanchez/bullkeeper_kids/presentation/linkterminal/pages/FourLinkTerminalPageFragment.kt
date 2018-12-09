package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.LinkDeviceTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.ILinkDeviceTutorialHandler
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject


/**
 * Four Link Terminal Page Fragment
 */
class FourLinkTerminalPageFragment: SupportPageFragment<LinkDeviceTutorialComponent>() {

    /**
     * Link Device Tutorial Handler
     */
    lateinit var linkDeviceTutorialHandler: ILinkDeviceTutorialHandler

    /**
     * Dependencies
     * ==================
     */

    // Four Link Terminal View Model
    @Inject
    lateinit var fourLinkTerminalViewModel: FourLinkTerminalViewModel

    /**
     * Preference Repository
     */
    @Inject
    lateinit var preferenceRepository: IPreferenceRepository

    /**
     * App Context
     */
    @Inject
    lateinit var appContext: Context


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int
            = R.layout.four_link_terminal_page_fragment_layout

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): LinkDeviceTutorialComponent {
        val linkDeviceTutorialComponent =
                LinkDeviceTutorialComponent::class.java
                .cast((activity as HasComponent<LinkDeviceTutorialComponent>)
                        .component)
        linkDeviceTutorialComponent.inject(this)
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
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalAppsSyncObserver = Observer<Int> {
            linkDeviceTutorialHandler.hideProgressDialog()
            linkDeviceTutorialHandler.showNoticeDialog(R.string.terminal_successfully_linked, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    linkDeviceTutorialHandler.releaseFocus()
                }
            })
        }

        fourLinkTerminalViewModel.totalAppsSync.observe(this, totalAppsSyncObserver)

        val errorSyncAppsObserver = Observer<Failure> {
            linkDeviceTutorialHandler.hideProgressDialog()
            linkDeviceTutorialHandler.showNoticeDialog(R.string.terminal_linked_failed)
        }

        fourLinkTerminalViewModel.errorSyncApps.observe(this, errorSyncAppsObserver)

    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")
    }

    /**
     * When Phase Is Showed
     */
    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
        if(preferenceRepository.getPrefTerminalIdentity() !=
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefKidIdentity() !=
                IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE) {

            linkDeviceTutorialHandler.showProgressDialog(R.string.generic_loading_text)
            // Sync Apps
            fourLinkTerminalViewModel.syncApps()
        }
    }


    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText,
                        Direction.LEFT_TO_RIGHT, 0.2f)
        )
    }

}