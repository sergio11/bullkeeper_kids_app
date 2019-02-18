package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.KidGuardianDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Children Service
 *
 * GET /api/v1/children/{id}/guardians/confirmed GET_CONFIRMED_GUARDIANS_FOR_KID
 */
interface IChildrenService {

    /**
     * Get Confirmed Guardians For Kid
     * @param id
     */
    @GET("children/{id}/guardians/confirmed")
    fun getConfirmedGuardiansForKid(
            @Path("id") id: String
    ): Deferred<APIResponse<Iterable<KidGuardianDTO>>>

}


