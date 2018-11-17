package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAuthService
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account.AuthenticateInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInViewModel
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.services.impl.AuthenticatorServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Authentication Module
 */
@Module
class AuthModule {


    /**
     * Provide Auth Service
     */
    @Provides
    @PerActivity
    fun provideAuthService(retrofit: Retrofit): IAuthService =
            retrofit.create(IAuthService::class.java)

    /**
     * Provide Authenticator Service
     */
    @Provides
    @PerActivity
    fun provideAuthenticatorService(authService: IAuthService): IAuthenticatorService =
            AuthenticatorServiceImpl(authService)

    /**
     * Provide Authenticate Interact
     */
    @Provides
    @PerActivity
    fun provideAuthenticateInteract(retrofit: Retrofit, authenticatorService: IAuthenticatorService): AuthenticateInteract =
            AuthenticateInteract(retrofit, authenticatorService)


    /**
     * Provide SignInViewModel
     */
    @Provides
    @PerActivity
    fun provideSignInViewModel(authenticateInteract: AuthenticateInteract,
                               preferenceRepository: IPreferenceRepository): SignInViewModel
            = SignInViewModel(authenticateInteract, preferenceRepository)

}