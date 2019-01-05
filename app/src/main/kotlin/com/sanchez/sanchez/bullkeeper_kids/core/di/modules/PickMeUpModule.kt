package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.kidrequest.SendRequestInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.pickmeup.PickMeUpViewModel
import dagger.Module
import dagger.Provides

/**
 * Pick Me Up Module
 */
@Module(includes = [KidRequestModule::class])
class PickMeUpModule {

    /**
     * Provide Pick Me View Model
     */
    @Provides
    @PerActivity
    fun providePickMeUpViewModel(
            sendRequestInteract: SendRequestInteract,
            getAddressFromCurrentLocationInteract: GetAddressFromCurrentLocationInteract): PickMeUpViewModel
            = PickMeUpViewModel(sendRequestInteract, getAddressFromCurrentLocationInteract)
}