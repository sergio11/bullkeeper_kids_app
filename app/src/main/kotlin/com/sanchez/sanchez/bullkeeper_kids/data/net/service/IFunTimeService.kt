package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.FunTimeScheduledDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Fun Time Service
 */
interface IFunTimeService {

    /**
     * Get Fun Time Scheduled
     * @param kid
     * @param terminal
     * @return
     */
    @GET("children/{kid}/terminal/{terminal}/funtime-scheduled")
    fun getFunTimeScheduled(
            @Path("kid") kid: String,
            @Path("terminal") terminal: String
    ): Deferred<APIResponse<FunTimeScheduledDTO>>
}