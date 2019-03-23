package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddTerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveTerminalStatusDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Terminal Service
 *
 */
interface ITerminalService {

    /**
     * Save Terminal
     * @param kid
     * @param addTerminalDTO
     */
    @POST("children/{id}/terminal")
    fun saveTerminal(@Path("id") kid: String, @Body addTerminalDTO: AddTerminalDTO)
            : Deferred<APIResponse<TerminalDTO>>


    /**
     * Get Terminal Detail
     * @param kid
     * @param terminal
     */
    @GET("children/{id}/terminal/{terminal}")
    fun getTerminalDetail(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<TerminalDTO>>

    /**
     * Save Terminal Status
     * @param kid
     * @param terminal
     */
    @POST("children/{kid}/terminal/{terminal}/status")
    fun saveTerminalStatus(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body status: SaveTerminalStatusDTO
    ): Deferred<APIResponse<String>>

    /**
     * Detach Terminal
     * @param kid
     * @param terminal
     */
    @POST("children/{kid}/terminal/{terminal}/detach")
    fun detachTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ): Deferred<APIResponse<String>>
    
}