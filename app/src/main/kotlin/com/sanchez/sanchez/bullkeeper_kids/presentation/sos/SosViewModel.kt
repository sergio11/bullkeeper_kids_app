package com.sanchez.sanchez.bullkeeper_kids.presentation.sos

import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.RequestTypeEnum
import java.util.*
import javax.inject.Inject

/**
 * Sos View Model
 */
class SosViewModel
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
     * Sos Request Failure
     */
    var sosRequestFailure: MutableLiveData<Failure> = MutableLiveData()

    /**
     * Sos Request Expired At
     */
    var sosRequestExpiredAt: MutableLiveData<Date> = MutableLiveData()

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
     * Activate SOS
     * @param address
     */
    fun activateSos(){
        sendRequestInteract(SendRequestInteract.Params(
                type = RequestTypeEnum.SOS.name,
                address = addressFromCurrentLocation.value)) {
            it.either(::onSosRequestFailed, ::onSosRequestSuccess)
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
     * On SOS Request Failed
     */
    private fun onSosRequestFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        sosRequestFailure.value = failure
    }

    /**
     * On Sos Request Success
     */
    private fun onSosRequestSuccess(expiredAt: Date) {
        Preconditions.checkNotNull(expiredAt, "Expired At can not be null")
        sosRequestExpiredAt.value = expiredAt
    }
}