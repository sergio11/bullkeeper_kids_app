package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.showLongMessage
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_pick_me_up.*
import javax.inject.Inject

/**
 * Pick Me Up Activity Fragment
 */
class PickMeUpActivityFragment : BaseFragment() {

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
    override fun layoutId(): Int = R.layout.fragment_pick_me_up

    /**
     * Pick Me Up Stream Id
     */
    private var pickMeUpStreamId: Int = -1

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        activatePickMeUp.setOnClickListener {
            context!!.showLongMessage("Activate PickMeUp")
            pickMeUpStreamId = soundManager.playSound(ISoundManager.PICK_ME_UP_SOUND)
            activatePickMeUp.isEnabled = false
            activatePickMeUp.text = getString(R.string.pick_me_up_button_activate_text)
        }

        //currentLocationText.text = "Calle Carmen Laforet 23 - Ávila"
    }

    /**
     * On Destroy View
     */
    override fun onDestroyView() {
        super.onDestroyView()
        soundManager.stopSound(pickMeUpStreamId)
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
