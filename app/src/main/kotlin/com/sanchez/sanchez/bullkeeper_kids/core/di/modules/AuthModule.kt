package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.arch.lifecycle.ViewModel
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.core.di.viewmodel.ViewModelKey
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account.AuthenticateInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInViewModel
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.AuthenticatorServiceImpl
import dagger.Module
import dagger.Provides

/**
 * Authentication Module
 */
@Module
class AuthModule {

    /**
     * Provide Authenticator Service
     */
    @Provides
    @PerActivity
    fun provideAuthenticatorService(): IAuthenticatorService =
            AuthenticatorServiceImpl()

    /**
     * Provide Authenticate Interact
     */
    @Provides
    @PerActivity
    fun provideAuthenticateInteract(authenticatorService: IAuthenticatorService): AuthenticateInteract =
            AuthenticateInteract(authenticatorService)


    /**
     * Provide SignInViewModel
     */
    @Provides
    @PerActivity
    fun provideSignInViewModel(authenticateInteract: AuthenticateInteract): SignInViewModel
        = SignInViewModel(authenticateInteract)

}