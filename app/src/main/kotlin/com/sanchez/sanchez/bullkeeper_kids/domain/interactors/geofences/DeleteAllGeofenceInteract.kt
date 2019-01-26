package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Delete All Geofence Interact
 */
class DeleteAllGeofenceInteract
    @Inject constructor(
            private val deviceGeofenceService: IDeviceGeofenceService,
            private val geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
            ): UseCase<Unit, UseCase.None>(retrofit) {

    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {
        // Delete Geofence
        deviceGeofenceService.deleteAllGeofences()
        // Delete Geofences
        geofenceRepository.deleteAll()
    }
}