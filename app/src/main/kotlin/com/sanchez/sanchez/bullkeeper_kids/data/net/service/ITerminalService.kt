package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddTerminalDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.TerminalDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Terminal Service
 */
interface ITerminalService {

    /**
     * Save Terminal
     */
    @POST("children/{son}/terminal")
    fun saveTerminal(@Path("son") sonId: String, @Body addTerminalDTO: AddTerminalDTO)
            : Deferred<APIResponse<TerminalDTO>>
}