package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveSmsDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.SmsDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * SMS Service
 * POST /api/v1/children/{kid}/terminal/{terminal}/sms/delete DELETE_SMS_FROM_TERMINAL
 */
interface ISmsService {

    /**
     * Delete All Apps Installed
     */
    @DELETE("children/{kid}/terminal/{terminal}/sms")
    fun deleteAllSmsFromTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<String>>

    /**
     * Get SMS From Terminal
     */
    @GET("children/{kid}/terminal/{terminal}/sms")
    fun getSmsFromTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<SmsDTO>>

    /**
     * Delete SMS By Id
     */
    @DELETE("children/{kid}/terminal/{terminal}/sms/{sms}")
    fun deleteAppInstalledById(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Path("sms") sms: String) : Deferred<APIResponse<String>>

    /**
     * Save SMS in the terminal
     */
    @POST("children/{kid}/terminal/{terminal}/sms")
    fun saveSmsInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body sms: List<SaveSmsDTO>
    ): Deferred<APIResponse<List<SmsDTO>>>

    /**
     * Delete SMS from terminal
     */
    @POST("children/{kid}/terminal/{terminal}/sms/delete")
    fun deleteSmsFromTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body smsList: List<String>
    ): Deferred<APIResponse<String>>

}