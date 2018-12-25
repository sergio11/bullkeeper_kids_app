package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosViewModel
import dagger.Module
import dagger.Provides

/**
 * Pick Me Up Module
 */
@Module
class PickMeUpModule {


    /**
     * Provide Sos View Model
     */
    @Provides
    @PerActivity
    fun providePickMeUpViewModel(getAddressFromCurrentLocationInteract:
                            GetAddressFromCurrentLocationInteract
                            ): SosViewModel
            = SosViewModel(getAddressFromCurrentLocationInteract)
}