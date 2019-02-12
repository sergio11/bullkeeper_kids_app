package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.PhoneNumberBlockedDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Phone Number Service
 */
interface IPhoneNumberService {

    /**
     * Get Phone Number Blocked
     */
    @GET("children/{id}/terminal/{terminal}/phonenumbers-blocked")
    fun getPhoneNumberBlocked(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<List<PhoneNumberBlockedDTO>>>

}