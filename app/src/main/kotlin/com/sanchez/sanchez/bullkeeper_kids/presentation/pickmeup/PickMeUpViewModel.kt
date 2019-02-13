package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.RequestTypeEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import java.util.*
import javax.inject.Inject

/**
 * Pick Me Up View Model
 */
class PickMeUpViewModel
    @Inject constructor(
            private val sendRequestInteract: SendRequestInteract,
            private val getAddressFromCurrentLocationInteract: GetAddressFromCurrentLocationInteract) : BaseViewModel()  {

    /**
     * Address From Current Location
     */
    var addressFromCurrentLocation: MutableLiveData<String> = MutableLiveData()

    /**
     * Address Failure
     */
    var addressFailure: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Pick Me Up Request Failure
     */
    var pickMeUpRequestFailure: MutableLiveData<Failure> = MutableLiveData()

    /**
     * Pick Me Up Request Expired At
     */
    var pickMeUpRequestExpiredAt: MutableLiveData<Date> = MutableLiveData()

    override fun init() {}

    /**
     * Get Address From Current Location
     */
    fun getAddressFromCurrentLocation() {
        // Get All Packages Installed
        getAddressFromCurrentLocationInteract(UseCase.None()){
            it.either(::onAddressFromCurrentLocationFailed, ::onAddressFromCurrentLocationSuccess)
        }
    }

    /**
     * Activate Pick Me Up
     */
    fun activatePickMeUp(){
        sendRequestInteract(SendRequestInteract.Params(RequestTypeEnum.PICKMEUP.name)){
            it.either(::onPickMeUpRequestFailed, ::onPickMeUpRequestSuccess)
        }
    }

    /**
     * On Address From Current Location Failed
     */
    private fun onAddressFromCurrentLocationFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        addressFailure.value = failure
    }

    /**
     * On Address From CurrentLocation Success
     */
    private fun onAddressFromCurrentLocationSuccess(address: String) {
        Preconditions.checkNotNull(address, "Address can not be null")
        addressFromCurrentLocation.value = address

    }

    /**
     * On Pick Me Up Request Failed
     */
    private fun onPickMeUpRequestFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        pickMeUpRequestFailure.value = failure
    }

    /**
     * On Pick Me Up Request Success
     */
    private fun onPickMeUpRequestSuccess(expiredAt: Date) {
        Preconditions.checkNotNull(expiredAt, "Expired At can not be null")
        pickMeUpRequestExpiredAt.value = expiredAt
    }
}