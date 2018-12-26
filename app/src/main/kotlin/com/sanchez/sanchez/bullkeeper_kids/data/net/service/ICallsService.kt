package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCallDetailDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.CallDetailDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Calls Service
 * POST /api/v1/children/{kid}/terminal/{terminal}/calls/delete DELETE_CALL_DETAILS_FROM_TERMINAL
 */
interface ICallsService {

    /**
     * Delete All History Calls
     */
    @DELETE("children/{kid}/terminal/{terminal}/calls")
    fun deleteAllHistoryCalls(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<String>>

    /**
     * Get All History calls in the terminal
     */
    @GET("children/{kid}/terminal/{terminal}/calls")
    fun getAllCallsInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<CallDetailDTO>>

    /**
     * Delete Call By Id
     */
    @DELETE("children/{kid}/terminal/{terminal}/calls/{call}")
    fun deleteCallDetailById(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Path("call") call: String) : Deferred<APIResponse<String>>

    /**
     * Save History Calls In The terminal
     */
    @POST("children/{kid}/terminal/{terminal}/calls")
    fun saveHistoryCallsInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body calls: List<SaveCallDetailDTO>
    ): Deferred<APIResponse<List<CallDetailDTO>>>

    /**
     * Add Call Details From Terminal
     */
    @POST("children/{kid}/terminal/{terminal}/calls/add")
    fun addCallDetailsFromTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body calls: SaveCallDetailDTO

    ): Deferred<APIResponse<CallDetailDTO>>

    /**
     * Delete Call Details From Terminal
     */
    @POST("children/{kid}/terminal/{terminal}/calls/delete")
    fun deleteCallDetailsFromTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body callsList: List<String>
    ): Deferred<APIResponse<String>>
}