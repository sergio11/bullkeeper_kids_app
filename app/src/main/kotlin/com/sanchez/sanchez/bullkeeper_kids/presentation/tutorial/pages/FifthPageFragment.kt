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
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import kotlinx.android.synthetic.main.fifth_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Fifth Page Fragment
 */
class FifthPageFragment: SupportPageFragment<AppTutorialComponent>() {

    /**
     * Dependencies
     * ================
     */

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * Usage Stats Service
     */
    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * App Tutorial Handler
     */
    private lateinit var appTutorialHandler: IAppTutorialHandler

    /**
     * State
     * =============
     */

    private var requestUsageStatsInProgress: Boolean = false


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.fifth_page_fragment_layout

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
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("When Phase Is Hidden")

        if(currentPosition > pagePosition && !usageStatsService.isUsageStatsAllowed())
            appTutorialHandler.showNoticeDialog(R.string.fifth_page_usage_stats_no_granted, object : NoticeDialogFragment.NoticeDialogListener {
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
        if (usageStatsService.isUsageStatsAllowed())
            appTutorialHandler.showNoticeDialog(R.string.fifth_page_usage_stats_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    appTutorialHandler.releaseFocus()
                }
            })
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Enable Usage Stats
        enableUsageStats.setOnClickListener {

            if (!usageStatsService.isUsageStatsAllowed()) {
                requestUsageStatsInProgress = true
                appTutorialHandler.showUsageAccessSettings()
            } else
                appTutorialHandler.showNoticeDialog(R.string.fifth_page_usage_stats_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
                    override fun onAccepted(dialog: DialogFragment) {
                        appTutorialHandler.releaseFocus()
                    }
                })
        }
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
                TransformItem.create(R.id.titleText,
                        Direction.LEFT_TO_RIGHT, 0.7f),
                TransformItem.create(R.id.contentText,
                        Direction.RIGHT_TO_LEFT, 0.7f),
                TransformItem.create(R.id.enableUsageStats,
                        Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()

        if(requestUsageStatsInProgress && usageStatsService.isUsageStatsAllowed()) {
            appTutorialHandler.showNoticeDialog(R.string.fifth_page_usage_stats_granted, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    requestUsageStatsInProgress = false
                    appTutorialHandler.releaseFocus()
                }
            })
        }

    }
}