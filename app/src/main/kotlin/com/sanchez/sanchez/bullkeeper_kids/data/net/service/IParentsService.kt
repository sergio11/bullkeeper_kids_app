package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.SonDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


/**
 * Parents Service
 */
interface IParentsService {

    /**
     * Get Self Children
     */
    @GET("parents/self/children")
    fun getSelfChildren(): Deferred<APIResponse<List<SonDTO>>>
}