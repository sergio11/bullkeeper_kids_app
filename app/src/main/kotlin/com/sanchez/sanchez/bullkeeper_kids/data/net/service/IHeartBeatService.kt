package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.NotifyTerminalHeartBeatDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * HeartBeat Service
 */
interface IHeartBeatService {

    /**
     * Notify Terminal Heart Beat
     */
    @POST("children/{kid}/terminal/{terminal}/heartbeat")
    fun notifyTerminalHeartbeat(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body heartbeat: NotifyTerminalHeartBeatDTO
    ): Deferred<APIResponse<String>>
}