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
    @GET("children/{id}/geofences")
    fun getGeofencesByKid(
            @Path("id") kid: String
    ): Deferred<APIResponse<List<GeofenceDTO>>>


    /**
     * Save Geofence Alerts
     * @param kid
     * @param geofenceAlert
     * @return
     */
    @POST("children/{id}/geofences/alerts")
    fun saveGeofenceAlerts(
            @Path("id") kid: String,
            @Body geofenceAlert: List<SaveGeofenceAlertDTO>
    ): Deferred<APIResponse<List<GeofenceAlertDTO>>>
}