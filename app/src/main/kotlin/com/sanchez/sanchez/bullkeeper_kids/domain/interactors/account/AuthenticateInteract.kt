package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.account

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.RetrofitException
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Authenticate Interact
 */
@PerActivity
class AuthenticateInteract
    @Inject constructor(retrofit: Retrofit,
                        private val authenticateService: IAuthenticatorService):
        UseCase<AuthenticateInteract.AuthenticationResponse, AuthenticateInteract.IParams>(retrofit){

    /**
     * Bad Credentials
     */
    private val BAD_CREDENTIALS_CODE_NAME = "BAD_CREDENTIALS"

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: IParams): AuthenticationResponse {
        Preconditions.checkNotNull(params, "params can not be null")

       val response = when (params) {

           /**
            * User Credentials
            */
            is UserCredentials -> authenticateService.getAuthorizationToken(params.email, params.password).await()

           /**
            * Social Token
            */
            is SocialToken -> authenticateService.getAuthorizationToken(params.token).await()

           /**
            * Invalid Params
            */
            else -> throw IllegalArgumentException("Invalid Params")

        }


        return AuthenticationResponse(response.data?.identity, response.data?.token)
    }


    /**
     * On Api Exception Ocurred
     */
    override fun onApiExceptionOcurred(apiException: RetrofitException, response: APIResponse<*>): Failure {
        return if(response.codeName?.equals(BAD_CREDENTIALS_CODE_NAME)!!)
            IncorrectCredentials() else super.onApiExceptionOcurred(apiException, response)
    }


    /**
     * Params
     */
    interface IParams {}

    data class UserCredentials(val email: String, val password: String): IParams
    data class SocialToken(val token: String): IParams

    /**
     * Authentication Response
     */
    data class AuthenticationResponse(
            val identity: String?,
            val token: String?
    )


    class IncorrectCredentials: Failure.FeatureFailure()


}