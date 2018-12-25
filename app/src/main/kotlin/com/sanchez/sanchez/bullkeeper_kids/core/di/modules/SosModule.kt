package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.location.GetAddressFromCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.sos.SosViewModel
import dagger.Module
import dagger.Provides

/**
 * Sos Module
 */
@Module
class SosModule {


    /**
     * Provide Sos View Model
     */
    @Provides
    @PerActivity
    fun provideSosViewModel(getAddressFromCurrentLocationInteract:
                            GetAddressFromCurrentLocationInteract
                            ): SosViewModel
            = SosViewModel(getAddressFromCurrentLocationInteract)
}