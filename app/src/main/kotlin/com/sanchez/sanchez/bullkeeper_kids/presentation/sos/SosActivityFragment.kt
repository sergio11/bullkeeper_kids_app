package com.sanchez.sanchez.bullkeeper_kids.presentation.sos

import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.showLongMessage
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_sos.*
import javax.inject.Inject

/**
 * Sos Activity Fragment
 */
class SosActivityFragment : BaseFragment() {

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_sos

    /**
     * Sos Stream Id
     */
    var sosStreamId: Int = -1

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        activateSos.setOnClickListener {
            context!!.showLongMessage("Activate SOS")
            sosStreamId = soundManager.playSound(ISoundManager.SOS_ALARM_SOUND, true)
            activateSos.isEnabled = false
            activateSos.text = getString(R.string.sos_button_activate_text)
        }

        currentLocationText.text = "Calle Carmen Laforet 23 - Ávila"
    }

    /**
     * On Destroy View
     */
    override fun onDestroyView() {
        super.onDestroyView()
        soundManager.stopSound(sosStreamId)
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
