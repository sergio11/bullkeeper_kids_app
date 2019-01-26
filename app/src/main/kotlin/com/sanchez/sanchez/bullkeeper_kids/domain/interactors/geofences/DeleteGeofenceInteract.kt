package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete Geofence Interact
 */
class DeleteGeofenceInteract
    @Inject constructor(
            private val deviceGeofenceService: IDeviceGeofenceService,
            private val geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
            ): UseCase<Unit, DeleteGeofenceInteract.Params>(retrofit) {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    override suspend fun onExecuted(params: Params) {

        // Delete Geofences
        geofenceRepository.deleteById(params.ids)

        // Delete Geofence
        deviceGeofenceService.deleteGeofences(params.ids)
    }

    /**
     * Params
     */
    data class Params(
            // Goefences
            var ids: List<String>
    )
}