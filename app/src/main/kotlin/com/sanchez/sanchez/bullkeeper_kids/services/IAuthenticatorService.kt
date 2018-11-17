package com.sanchez.sanchez.bullkeeper_kids.services

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.JwtAuthenticationResponseDTO
import kotlinx.coroutines.Deferred

/**
 * Authenticator Service
 */
interface IAuthenticatorService {

    /**
     * User Logged In
     */
    fun userLoggedIn(): Boolean

    /**
     * Get Authorization Token
     */
    fun getAuthorizationToken(email: String, password: String): Deferred<APIResponse<JwtAuthenticationResponseDTO>>

    /**
     * Get Authorization Token
     */
    fun getAuthorizationToken(token: String): Deferred<APIResponse<JwtAuthenticationResponseDTO>>

}