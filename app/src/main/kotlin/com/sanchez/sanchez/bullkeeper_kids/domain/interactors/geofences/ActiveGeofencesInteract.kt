package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Active Geofences Interact
 */
class ActiveGeofencesInteract
    @Inject constructor(
            retrofit: Retrofit,
            private val geofencesRepository: IGeofenceRepository,
            private val deviceGeofenceService: IDeviceGeofenceService
    ): UseCase<Unit, UseCase.None>(retrofit) {


    /**
     * On Executed
     */
    override suspend fun onExecuted(params: None) {
        // Delete All Geofences
        deviceGeofenceService.deleteAllGeofences()

        // Add Geofences
        deviceGeofenceService.addGeofence(geofencesRepository.list())
    }
}