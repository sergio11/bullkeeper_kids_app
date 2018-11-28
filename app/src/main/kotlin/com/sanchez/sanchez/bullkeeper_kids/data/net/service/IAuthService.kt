package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.JwtAuthenticationRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.JwtSocialAuthenticationRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.JwtAuthenticationResponseDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Auth Service
 */
interface IAuthService {

    /**
     * Get Authorization Token
     * @param authorizationRequest
     * @return
     */
    @POST("guardians/auth/")
    fun getAuthorizationToken(
            @Body authorizationRequest: JwtAuthenticationRequestDTO): Deferred<APIResponse<JwtAuthenticationResponseDTO>>


    /**
     * Get Authorization Token By Facebook
     * @param authorizationRequest
     * @return
     */
    @POST("guardians/auth/facebook")
    fun getAuthorizationTokenByFacebook(
            @Body authorizationRequest: JwtSocialAuthenticationRequestDTO): Deferred<APIResponse<JwtAuthenticationResponseDTO>>
}