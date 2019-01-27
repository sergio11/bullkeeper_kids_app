package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveGeofenceAlertDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.GeofenceDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.GeofenceAlertDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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


    /**
     * Save Geofence Alert
     * @param kid
     * @param geofence
     * @param geofenceAlert
     * @return
     */
    @POST("children/{kid}/geofences/{geofence}/alerts")
    fun saveGeofenceAlert(
            @Path("kid") kid: String,
            @Path("geofence") geofence: String,
            @Body geofenceAlert: SaveGeofenceAlertDTO
    ): Deferred<APIResponse<GeofenceAlertDTO>>
}