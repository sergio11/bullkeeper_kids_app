package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveCurrentLocationDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.CurrentLocationDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Location Service
 */
interface ILocationService {

    /**
     * Save Current Location
     */
    @POST("children/{id}/location")
    fun saveCurrentLocation(
            @Path("id")  kid: String,
            @Body currentLocationDTO: SaveCurrentLocationDTO
    ): Deferred<APIResponse<CurrentLocationDTO>>
}