package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddTerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Terminal Service
 * GET /api/v1/children/{kid}/terminal/{terminal} GET_TERMINAL_DETAIL
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