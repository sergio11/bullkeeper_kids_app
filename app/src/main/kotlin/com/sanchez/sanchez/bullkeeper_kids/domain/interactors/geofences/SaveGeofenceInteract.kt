package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.toStringFormat
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

/**
 * Save Geofence Interact
 */
class SaveGeofenceInteract
    @Inject constructor(
            private val context: Context,
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
            geofenceSaved.address = params.address
            geofenceSaved.lat = params.lat
            geofenceSaved.log = params.log
            geofenceSaved.radius = params.radius
            geofenceSaved.transitionType = params.transitionType
            geofenceSaved.kid = params.kid
            geofenceSaved.isEnabled = params.isEnabled
            geofenceSaved.createAt = params.createAt.toStringFormat(
                    context.getString(R.string.date_time_format_2)
            )
            geofenceSaved.updateAt = params.updateAt.toStringFormat(
                    context.getString(R.string.date_time_format_2)
            )

        } else {

            geofenceSaved = GeofenceEntity(
                    identity = params.identity,
                    name = params.name,
                    lat = params.lat,
                    log = params.log,
                    radius = params.radius,
                    transitionType = params.transitionType,
                    kid = params.kid,
                    address = params.address,
                    isEnabled = params.isEnabled,
                    createAt = params.createAt.toStringFormat(
                            context.getString(R.string.date_time_format_2)
                    ),
                    updateAt = params.updateAt.toStringFormat(
                            context.getString(R.string.date_time_format_2)
                    )

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
            // Address
            var address: String,
            // Latitude
            var lat: Double,
            // Longitude
            var log: Double,
            // Radius
            var radius: Float,
            // Transition Type
            var transitionType: String,
            // Kid
            var kid: String,
            // Is Enabled
            var isEnabled: Boolean,
            // Create At
            var createAt: Date,
            // Update At
            var updateAt: Date

    )
}