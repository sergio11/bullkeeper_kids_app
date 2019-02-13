package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.arch.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account.AuthenticateInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import javax.inject.Inject

/**
 * Sign In View Model
 */
class SignInViewModel
    @Inject constructor(private val authenticateInteract: AuthenticateInteract,
                        private val preferenceRepository: IPreferenceRepository) : BaseViewModel()  {

    /**
     * Sig In Success
     */
    var sigInSuccess: MutableLiveData<SignInSuccessView> = MutableLiveData()

    /**
     * Sig In Failure
     */
    var sigInFailure: MutableLiveData<Failure> = MutableLiveData()


    override fun init(){}


    /**
     * Autenticate
     */
    fun authenticate(email: String, password: String) {
        Preconditions.checkNotNull(email, "Email can not be null")
        Preconditions.checkState(email.isNotEmpty(), "Email can not be empty")
        Preconditions.checkNotNull(password, "Password can not be null")
        Preconditions.checkState(password.isNotEmpty(), "Password can not be empty")

        // Get All Packages Installed
        authenticateInteract(AuthenticateInteract.UserCredentials(email, password)){
            it.either(::onAuthenticationFailed, ::onAuthenticationSuccess)
        }
    }

    /**
     * Authenticate with Access Token
     */
    fun authenticate(accessToken: AccessToken) {
        Preconditions.checkNotNull(accessToken, "AccessToken can not be null")
        Preconditions.checkNotNull(accessToken.token, "Token can not be null")
        // Get All Packages Installed
        authenticateInteract(AuthenticateInteract.SocialToken(accessToken.token)){
            it.either(::onAuthenticationFailed, ::onAuthenticationSuccess)
        }
    }


    /**
     * On Authentication Failed
     */
    private fun onAuthenticationFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        sigInFailure.value = failure
    }

    /**
     * On Authentication Succes
     */
    private fun onAuthenticationSuccess(authenticationResponse: AuthenticateInteract.AuthenticationResponse) {
        Preconditions.checkNotNull(authenticationResponse, "Auth Token can not be null")
        preferenceRepository.setAuthToken(authenticationResponse.token!!)
        preferenceRepository.setPrefCurrentUserIdentity(authenticationResponse.identity!!)
        sigInSuccess.value = SignInSuccessView(authenticationResponse.token)

    }

}