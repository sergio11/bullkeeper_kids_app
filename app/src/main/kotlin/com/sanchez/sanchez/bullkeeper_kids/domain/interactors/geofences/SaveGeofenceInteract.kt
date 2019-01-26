package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Save Geofence Interact
 */
class SaveGeofenceInteract
    @Inject constructor(
            private val deviceGeofenceService: IDeviceGeofenceService,
            private val geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
            ): UseCase<Unit, SaveGeofenceInteract.Params>(retrofit) {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    override suspend fun onExecuted(params: Params) {

        var geofenceSaved = geofenceRepository.findById(params.identity)

        if(geofenceSaved != null) {

            geofenceSaved.name = params.name
            geofenceSaved.lat = params.lat
            geofenceSaved.log = params.log
            geofenceSaved.radius = params.radius
            geofenceSaved.transitionType = params.transitionType
            geofenceSaved.kid = params.kid

        } else {

            geofenceSaved = GeofenceEntity(
                    params.identity, params.name,
                    params.lat, params.log, params.radius, params.transitionType,
                    params.kid
            )
        }

        // Save Geofence
        geofenceRepository.save(geofenceSaved)

        // Configure Geofence in the terminal
        deviceGeofenceService.addGeofence(geofenceSaved)
    }

    /**
     * Params
     */
    data class Params(
            // Identity
            var identity: String,
            // Name
            var name: String,
            // Latitude
            var lat: Double,
            // Longitude
            var log: Double,
            // Radius
            var radius: Float,
            // Transition Type
            var transitionType: String,
            // Kid
            var kid: String
    )
}