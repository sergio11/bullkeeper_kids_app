package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosViewModel
import dagger.Module
import dagger.Provides

/**
 * Sos Module
 */
@Module(includes = [KidRequestModule::class])
class SosModule {

    /**
     * Provide Sos View Model
     */
    @Provides
    @PerActivity
    fun provideSosViewModel(
            sendRequestInteract: SendRequestInteract,
            getAddressFromCurrentLocationInteract: GetAddressFromCurrentLocationInteract): SosViewModel
            = SosViewModel(sendRequestInteract, getAddressFromCurrentLocationInteract)
}