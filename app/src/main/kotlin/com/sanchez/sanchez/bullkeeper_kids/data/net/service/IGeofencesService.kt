package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.GeofenceDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Geofences Service
 */
interface IGeofencesService {

    /**
     * Get Geofences By Kid
     * @param kid
     * @return
     */
    @GET("children/{kid}/geofences")
    fun getGeofencesByKid(
            @Path("kid") kid: String
    ): Deferred<APIResponse<List<GeofenceDTO>>>
}