package com.sanchez.sanchez.bullkeeper_kids.presentation.timebank

import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.showLongMessage
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_time_bank.*
import java.util.*
import javax.inject.Inject

/**
 * Time Bank Activity Fragment
 */
class TimeBankActivityFragment : BaseFragment() {

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
    override fun layoutId(): Int = R.layout.fragment_time_bank

    /**
     * Time Bank Stream Id
     */
    private var timeBankStreamId: Int = -1

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()
9
        saveOnTimeBank.text = String.format(Locale.getDefault(),
                getString(R.string.time_bank_button_text),
                1, 5)

        saveOnTimeBank.setOnClickListener {
            context!!.showLongMessage("Activate PickMeUp")
            timeBankStreamId = soundManager.playSound(ISoundManager.TIME_BANK_SOUND)
            saveOnTimeBank.isEnabled = false
            saveOnTimeBank.text = getString(R.string.time_bank_button_activate_text)
        }

        //currentLocationText.text = "Calle Carmen Laforet 23 - Ávila"
    }

    /**
     * On Destroy View
     */
    override fun onDestroyView() {
        super.onDestroyView()
        soundManager.stopSound(timeBankStreamId)
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
