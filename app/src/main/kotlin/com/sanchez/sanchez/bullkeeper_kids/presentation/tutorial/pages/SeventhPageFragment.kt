package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.content.Context
import android.os.Bundle
import android.view.View
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.SupportPageFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject
import android.support.v4.app.DialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.services.IGeolocationService
import kotlinx.android.synthetic.main.seventh_page_fragment_layout.*


/**
 * Seventh Page Fragment
 */
class SeventhPageFragment: SupportPageFragment<AppTutorialComponent>() {

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
     * Geolocation Service
     */
    @Inject
    internal lateinit var geolocationService: IGeolocationService

    /**
     * State
     * =============
     */

    private var requestHighAccuraccyLocationInProgress: Boolean = false


    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.seventh_page_fragment_layout

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
        // Enable High Accuraccy Location
        enableHighAccuraccyLocation.setOnClickListener {
            if (!geolocationService.isHighAccuraccyLocationEnabled()) {
                requestHighAccuraccyLocationInProgress = true
                appTutorialHandler.showLocationSourceSettings()
            } else
                appTutorialHandler.showNoticeDialog(R.string.seventh_page_location_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
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

        if(requestHighAccuraccyLocationInProgress && geolocationService.isHighAccuraccyLocationEnabled()) {
            appTutorialHandler.showNoticeDialog(R.string.seventh_page_location_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
                override fun onAccepted(dialog: DialogFragment) {
                    requestHighAccuraccyLocationInProgress = false
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

        if(currentPosition > pagePosition && !geolocationService.isHighAccuraccyLocationEnabled())
            appTutorialHandler.showNoticeDialog(R.string.seventh_page_location_is_not_enabled, object : NoticeDialogFragment.NoticeDialogListener {
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

        if (geolocationService.isHighAccuraccyLocationEnabled())
            appTutorialHandler.showNoticeDialog(R.string.seventh_page_location_already_allowed, object : NoticeDialogFragment.NoticeDialogListener {
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
                TransformItem.create(R.id.enableHighAccuraccyLocation,
                        Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }



}