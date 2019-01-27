package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveGeofenceAlertDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGeofencesService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Save Geofence Alert Interact
 */
class SaveGeofenceAlertInteract
    @Inject constructor(
            private val geofencesService: IGeofencesService,
            retrofit: Retrofit
            ): UseCase<Unit, SaveGeofenceAlertInteract.Params>(retrofit) {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    override suspend fun onExecuted(params: Params) {

        geofencesService.saveGeofenceAlert(params.kid, params.geofence,
                SaveGeofenceAlertDTO(params.kid, params.geofence,
                        params.transitionType)).await()
    }

    /**
     * Params
     */
    data class Params(
            // Kid
            var kid: String,
            // Identity
            var geofence: String,
            // Transition Type
            var transitionType: String
    )
}