package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import kotlinx.android.synthetic.main.sixth_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException

/**
 * Sixth Page Fragment
 */
class SixthPageFragment: SupportPageFragment<AppTutorialComponent>() {

    /**
     * App Tutorial Handler
     */
    private lateinit var appTutorialHandler: IAppTutorialHandler

    /**
     * State
     * =============
     */

    private var requestAdminCapabilitiesInProgress: Boolean = false


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.sixth_page_fragment_layout

    /**
     * On Attach
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context !is IAppTutorialHandler)
            throw IllegalStateException("The context does not implement ILinkDeviceTutorialHandler")
        appTutorialHandler = context

    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableAdmin.setOnClickListener {

            if(!appTutorialHandler.isDevicePolicyManagerActive()) {
                requestAdminCapabilitiesInProgress = true
                appTutorialHandler.showDeviceAdminSettings()
            } else
                appTutorialHandler.showNoticeDialog(R.string.sixth_page_admin_is_already_enabled,
                        object : NoticeDialogFragment.NoticeDialogListener {
                            override fun onAccepted(dialog: DialogFragment) {
                                appTutorialHandler.releaseFocus()
                            }
                        })
        }
    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()

        if(requestAdminCapabilitiesInProgress && appTutorialHandler.isDevicePolicyManagerActive())
            appTutorialHandler.showNoticeDialog(R.string.sixth_page_admin_is_granted,
                object : NoticeDialogFragment.NoticeDialogListener {
                    override fun onAccepted(dialog: DialogFragment) {
                        requestAdminCapabilitiesInProgress = false
                        appTutorialHandler.releaseFocus()
                    }
                })

    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("When Phase is hidden")

        if(currentPosition > pagePosition) {
            if(!appTutorialHandler.isDevicePolicyManagerActive()) {
                appTutorialHandler.showNoticeDialog(R.string.sixth_page_admin_is_not_enabled, object : NoticeDialogFragment.NoticeDialogListener {
                    override fun onAccepted(dialog: DialogFragment) {
                        appTutorialHandler.requestFocus()
                    }
                })
            }
        }

    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("When Phase Is Showed")
        if (appTutorialHandler.isDevicePolicyManagerActive())
            appTutorialHandler.showNoticeDialog(R.string.sixth_page_admin_is_already_enabled,
                    object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    appTutorialHandler.releaseFocus()
                }
            })
    }

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): AppTutorialComponent? {
        val appTutorialComponent = AppTutorialComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        appTutorialComponent?.inject(this)
        return appTutorialComponent
    }

    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.contentText, Direction.RIGHT_TO_LEFT, 0.07f),
                TransformItem.create(R.id.enableAdmin, Direction.LEFT_TO_RIGHT, 0.2f)
        )
    }

}