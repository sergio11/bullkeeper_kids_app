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
    @POST("children/{kid}/terminal")
    fun saveTerminal(@Path("kid") kid: String, @Body addTerminalDTO: AddTerminalDTO)
            : Deferred<APIResponse<TerminalDTO>>


    /**
     * Get Terminal Detail
     */
    @GET("children/{kid}/terminal/{terminal}")
    fun getTerminalDetail(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<TerminalDTO>>


}