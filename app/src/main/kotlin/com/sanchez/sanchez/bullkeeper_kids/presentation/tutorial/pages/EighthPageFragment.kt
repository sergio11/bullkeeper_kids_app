package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import kotlinx.android.synthetic.main.eighth_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Eighth Page Fragment
 */
class EighthPageFragment: SupportPageFragment<AppTutorialComponent>() {

    /**
     * App Tutorial Handler
     */
    private lateinit var appTutorialHandler: IAppTutorialHandler

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * State
     * =============
     */

    private var requestEnableOverlapInProgress: Boolean = false


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.eighth_page_fragment_layout

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IAppTutorialHandler)
            throw IllegalStateException("The context should implement ILinkDeviceTutorialHandler")

        appTutorialHandler = context

    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enable Overlap
        enableOverlap.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                requestEnableOverlapInProgress = true
                appTutorialHandler.showManageOverlaySettings()
            } else
                appTutorialHandler.showNoticeDialog(R.string.eighth_page_overlap_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
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

        if(requestEnableOverlapInProgress && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context)) {
            appTutorialHandler.showNoticeDialog(R.string.eighth_page_overlap_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    requestEnableOverlapInProgress = false
                    appTutorialHandler.releaseFocus()
                }
            })
        }

    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("When Phase Is Hidden")

        if(currentPosition > pagePosition &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context))
            appTutorialHandler.showNoticeDialog(R.string.eighth_page_overlap_is_not_enabled, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    appTutorialHandler.requestFocus()
                }
            })
    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("When Phase Is Showed")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context))
            appTutorialHandler.showNoticeDialog(R.string.eighth_page_overlap_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
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
                .cast((activity as HasComponent<AppTutorialComponent>)
                        .component)
        appTutorialComponent.inject(this)
        return appTutorialComponent
    }

    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText,
                        Direction.LEFT_TO_RIGHT, 0.7f),
                TransformItem.create(R.id.contentText,
                        Direction.RIGHT_TO_LEFT, 0.7f),
                TransformItem.create(R.id.enableOverlap,
                        Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }
}