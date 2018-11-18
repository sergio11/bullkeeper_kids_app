package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveTerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Terminal Service
 */
interface ITerminalService {

    /**
     * Save Terminal
     */
    @POST("children/{son}/terminal")
    fun saveTerminal(@Body saveTerminalDTO: SaveTerminalDTO)
            : Deferred<APIResponse<TerminalDTO>>
}