package com.sanchez.sanchez.bullkeeper_kids.services.impl

import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.JwtAuthenticationRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.JwtSocialAuthenticationRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.JwtAuthenticationResponseDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IAuthService
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * Authenticator Service Impl
 */
class AuthenticatorServiceImpl
    @Inject constructor(private val authService: IAuthService): IAuthenticatorService {

    //Learning purpose: We assume the user is always logged in
    //Here you should put your own logic to return whether the user
    //is authenticated or not
    override fun userLoggedIn() = false

    /**
     * Get Authorization Token
     */
    override fun getAuthorizationToken(email: String, password: String): Deferred<APIResponse<JwtAuthenticationResponseDTO>> {
        Preconditions.checkNotNull(email, "Email can not be null")
        Preconditions.checkState(email.isNotEmpty(), "Email can not be empty")
        Preconditions.checkNotNull(password, "Password can not be null")
        Preconditions.checkState(password.isNotEmpty(), "Password can not be empty")

        return authService.getAuthorizationToken(JwtAuthenticationRequestDTO(email, password))
    }

    /**
     * Get Authorization  Token
     */
    override fun getAuthorizationToken(token: String): Deferred<APIResponse<JwtAuthenticationResponseDTO>> {
        Preconditions.checkNotNull(token, "Email can not be null")
        Preconditions.checkState(token.isNotEmpty(), "Email can not be empty")

        return authService.getAuthorizationTokenByFacebook(JwtSocialAuthenticationRequestDTO(token))

    }


}