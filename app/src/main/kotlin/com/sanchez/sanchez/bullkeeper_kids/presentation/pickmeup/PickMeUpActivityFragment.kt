package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.PickMeUpComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toStringFormat
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_pick_me_up.*
import java.lang.IllegalArgumentException
import java.util.*
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
            pickMeUpStreamId = soundManager.playSound(ISoundManager.PICK_ME_UP_SOUND)
            activatePickMeUp.isEnabled = false
            activatePickMeUp.text = getString(R.string.pick_me_up_button_in_progress_text)

            pickMeUpViewModel.activatePickMeUp()

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

        // Create the observer which updates the UI.
        val pickMeUpRequestFailureObserver = Observer<Failure> { failure ->
            when(failure){
                is SendRequestInteract.PreviousRequestHasNotExpiredException -> {
                    activatePickMeUp.isEnabled = false
                    activatePickMeUp.text = getString(R.string.pick_me_up_button_no_expired_text)
                }
                else -> {
                    soundManager.stopSound(pickMeUpStreamId)
                    activityHandler.showNoticeDialog(R.string.request_generic_error_occurred)
                    activatePickMeUp.isEnabled = true
                    activatePickMeUp.text = getString(R.string.pick_me_up_button_text)
                }
            }
        }

        // Create the observer which updates the UI.
        val pickMeUpRequestExpiredAtObserver = Observer<Date> { expiredAt ->


            expiredAt?.let {
                preferenceRepository.setPickMeUpRequestExpiredAt(
                        it.toStringFormat(getString(R.string.date_time_format))
                )
            }

            activatePickMeUp.isEnabled = false
            activatePickMeUp.text = getString(R.string.pick_me_up_button_no_expired_text)

        }

        pickMeUpViewModel.addressFromCurrentLocation.observe(this, addressFromCurrentLocationObserver)
        pickMeUpViewModel.addressFailure.observe(this, addressFromCurrentLocationFailureObserver)
        pickMeUpViewModel.pickMeUpRequestFailure.observe(this, pickMeUpRequestFailureObserver)
        pickMeUpViewModel.pickMeUpRequestExpiredAt.observe(this, pickMeUpRequestExpiredAtObserver)
    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()
        activityHandler.showProgressDialog(R.string.generic_loading_text)
        pickMeUpViewModel.getAddressFromCurrentLocation()

        val expiredAt = preferenceRepository.getPickMeUpRequestExpiredAt()

        if(!expiredAt.isEmpty() && expiredAt
                        .ToDateTime(getString(R.string.date_time_format)).after(Date())) {
            activatePickMeUp.isEnabled = false
            activatePickMeUp.text = getString(R.string.pick_me_up_button_no_expired_text)

        } else {
            activatePickMeUp.isEnabled = true
            activatePickMeUp.text = getString(R.string.pick_me_up_button_text)
        }

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
