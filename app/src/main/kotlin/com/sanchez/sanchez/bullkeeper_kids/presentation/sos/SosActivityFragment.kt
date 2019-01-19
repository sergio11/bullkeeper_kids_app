package com.sanchez.sanchez.bullkeeper_kids.presentation.sos

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SosComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToDateTime
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toStringFormat
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_sos.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject

/**
 * Sos Activity Fragment
 */
class SosActivityFragment : BaseFragment() {


    /**
     * Sos Activity Handler
     */
    private lateinit var activityHandler: ISosActivityHandler

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Sos View Model
     */
    @Inject
    internal lateinit var sosViewModel: SosViewModel

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
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is ISosActivityHandler)
            throw IllegalArgumentException("Context should implement ISosActivityHandler")

        activityHandler = context

    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        /**
         * Activate SOS
         */
        activateSos.setOnClickListener {
            sosStreamId = soundManager.playSound(ISoundManager.SOS_ALARM_SOUND, true)
            activateSos.isEnabled = false
            activateSos.text = getString(R.string.sos_button_in_progress_text)

            sosViewModel.activateSos()

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
        val sosRequestExpiredAtObserver = Observer<Date> { expiredAt ->

            expiredAt?.let {
                preferenceRepository.setSosRequestExpiredAt(it.toStringFormat(
                        getString(R.string.date_time_format)
                ))
            }

            activateSos.isEnabled = false
            activateSos.text = getString(R.string.sos_button_no_expired_text)

        }

        // Create the observer which updates the UI.
        val sosRequestFailureObserver = Observer<Failure> { failure ->
            when(failure){
                is SendRequestInteract.PreviousRequestHasNotExpiredException -> {
                    activateSos.isEnabled = false
                    activateSos.text = getString(R.string.sos_button_no_expired_text)
                }
                else -> {
                    soundManager.stopSound(sosStreamId)
                    activateSos.isEnabled = true
                    activateSos.text = getString(R.string.sos_button_text)
                    activityHandler.showNoticeDialog(R.string.request_generic_error_occurred)
                }
            }
        }

        sosViewModel.addressFromCurrentLocation.observe(this, addressFromCurrentLocationObserver)
        sosViewModel.addressFailure.observe(this, addressFromCurrentLocationFailureObserver)
        sosViewModel.sosRequestFailure.observe(this, sosRequestFailureObserver)
        sosViewModel.sosRequestExpiredAt.observe(this, sosRequestExpiredAtObserver)

    }

    /**
     * on Start
     */
    override fun onStart() {
        super.onStart()
        activityHandler.showProgressDialog(R.string.generic_loading_text)
        sosViewModel.getAddressFromCurrentLocation()

        val expiredAt = preferenceRepository.getSosRequestExpiredAt()

        Timber.d("SOS: Expired At -> %s", expiredAt)

        if(!expiredAt.isEmpty() && expiredAt
                        .ToDateTime(getString(R.string.date_time_format)).after(Date())) {
            activateSos.isEnabled = false
            activateSos.text = getString(R.string.sos_button_no_expired_text)

        } else {
            activateSos.isEnabled = true
            activateSos.text = getString(R.string.sos_button_text)
        }

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
        val sosComponent = SosComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)

        sosComponent?.inject(this)
    }
}
