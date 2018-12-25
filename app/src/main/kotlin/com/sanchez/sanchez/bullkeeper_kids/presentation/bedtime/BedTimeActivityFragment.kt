package com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.fragment_bed_time.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Bed Time Activity Fragment
 */
class BedTimeActivityFragment : BaseFragment() {

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IBedTimeActivityHandler


    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_bed_time

    /**
     * Bed Time Stream Id
     */
    private var bedTimeStreamId: Int = -1

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IBedTimeActivityHandler)
            throw IllegalArgumentException("Context must implement IBedTimeActivityHandler")

        activityHandler = context
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()
        activatePickMeUp.setOnClickListener {
            activityHandler.showPickMeUpScreen()
        }

        activateSos.setOnClickListener {
            activityHandler.showSosScreen()
        }

    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()
        bedTimeStreamId = soundManager.playSound(ISoundManager.BED_TIME_SOUND, true)
    }

    /**
     * On Stop
     */
    override fun onStop() {
        super.onStop()
        soundManager.stopSound(bedTimeStreamId)
    }


    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val applicationComponent = ApplicationComponent::class.java
                .cast((activity as HasComponent<ApplicationComponent>)
                        .component)

        applicationComponent.inject(this)
    }
}
