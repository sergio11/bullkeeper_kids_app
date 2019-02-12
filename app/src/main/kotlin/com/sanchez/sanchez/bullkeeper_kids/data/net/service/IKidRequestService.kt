package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddKidRequestDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.KidRequestDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * POST /api/v1/children/{id}/request ADD_REQUEST_FOR_KID
 */
interface IKidRequestService {

    /**
     * Add Request For Kid
     */
    @POST("children/{id}/request")
    fun addRequestForKid(
            @Path("id")  kid: String,
            @Body kidRequest: AddKidRequestDTO
    ): Deferred<APIResponse<KidRequestDTO>>

}