package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddTerminalDTO
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
     */
    @POST("children/{id}/terminal")
    fun saveTerminal(@Path("id") kid: String, @Body addTerminalDTO: AddTerminalDTO)
            : Deferred<APIResponse<TerminalDTO>>


    /**
     * Get Terminal Detail
     */
    @GET("children/{id}/terminal/{terminal}")
    fun getTerminalDetail(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<TerminalDTO>>


}