package com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup

import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import javax.inject.Inject

/**
 * Pick Me Up View Model
 */
class PickMeUpViewModel
    @Inject constructor(
            private val getAddressFromCurrentLocationInteract:
            GetAddressFromCurrentLocationInteract) : BaseViewModel()  {


    /**
     * Address From Current Location
     */
    var addressFromCurrentLocation: MutableLiveData<String> = MutableLiveData()

    /**
     * Address Failure
     */
    var addressFailure: MutableLiveData<Failure> = MutableLiveData()

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

}