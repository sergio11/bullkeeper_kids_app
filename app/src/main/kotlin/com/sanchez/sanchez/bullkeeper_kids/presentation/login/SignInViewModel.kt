package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account.AuthenticateInteract
import javax.inject.Inject

/**
 * Sign In View Model
 */
class SignInViewModel
    @Inject constructor(private val authenticateInteract: AuthenticateInteract) : BaseViewModel()  {


    var sigInSuccess: MutableLiveData<SignInSuccessView> = MutableLiveData()


    /**
     * Autenticate
     */
    fun authenticate(email: String, password: String) {
        Preconditions.checkNotNull(email, "Email can not be null")
        Preconditions.checkState(email.isNotEmpty(), "Email can not be empty")
        Preconditions.checkNotNull(password, "Password can not be null")
        Preconditions.checkState(password.isNotEmpty(), "Password can not be empty")

        // Get All Packages Installed
        authenticateInteract(AuthenticateInteract.Params(email, password)){
            it.either(::onAuthenticationFailed, ::onAuthenticationSuccess)
        }


    }


    /**
     * On Authentication Failed
     */
    private fun onAuthenticationFailed(failure: Failure) {


    }

    /**
     * On Authentication Succes
     */
    private fun onAuthenticationSuccess(authToken: String) {
        Preconditions.checkNotNull(authToken, "Auth Token can not be null")
        Preconditions.checkState(authToken.isNotEmpty(), "Auth Token can not be empty")

        sigInSuccess.value = SignInSuccessView(authToken)

    }



}