package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.PickMeUpComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.showLongMessage
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import kotlinx.android.synthetic.main.fragment_pick_me_up.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Pick Me Up Activity Fragment
 */
class PickMeUpActivityFragment : BaseFragment() {


    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IPickMeUpActivityHandler

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Pick Me Up View Model
     */
    @Inject
    internal lateinit var pickMeUpViewModel: PickMeUpViewModel


    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_pick_me_up

    /**
     * Pick Me Up Stream Id
     */
    private var pickMeUpStreamId: Int = -1


    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IPickMeUpActivityHandler)
            throw IllegalArgumentException("Context should implement IPickMeUpActivityHandler")

        activityHandler = context

    }

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

        // Create the observer which updates the UI.
        val addressFromCurrentLocationObserver = Observer<String> { address ->
            activityHandler.hideProgressDialog()
            if(!address.isNullOrEmpty())
                currentLocationText.text = address
            else
                currentLocationText.text = getString(R.string.sos_current_address_not_determined)
        }

        // Create the observer which updates the UI.
        val addressFromCurrentLocationFailureObserver = Observer<Failure> { failure ->
            activityHandler.hideProgressDialog()
            currentLocationText.text = getString(R.string.sos_current_address_not_determined)
        }

        pickMeUpViewModel.addressFromCurrentLocation.observe(this, addressFromCurrentLocationObserver)
        pickMeUpViewModel.addressFailure.observe(this, addressFromCurrentLocationFailureObserver)

    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()
        activityHandler.showProgressDialog(R.string.generic_loading_text)
        pickMeUpViewModel.getAddressFromCurrentLocation()

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
        val pickMeUpComponent = PickMeUpComponent::class.java
                .cast((activity as HasComponent<PickMeUpComponent>)
                        .component)

        pickMeUpComponent.inject(this)
    }
}
