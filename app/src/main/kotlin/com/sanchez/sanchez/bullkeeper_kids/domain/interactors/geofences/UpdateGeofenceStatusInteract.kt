package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Update Geofence Status Interact
 */
class UpdateGeofenceStatusInteract
    @Inject constructor(
            private val geofenceRepository: IGeofenceRepository,
            retrofit: Retrofit
            ): UseCase<Unit, UpdateGeofenceStatusInteract.Params>(retrofit) {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    override suspend fun onExecuted(params: Params) {
        geofenceRepository.findById(params.identity)?.let {
            it.isEnabled = params.isEnabled
            geofenceRepository.save(it)
        }
    }

    /**
     * Params
     */
    data class Params(
            // Identity
            var identity: String,
            // Kid
            var kid: String,
            // Is Enabled
            var isEnabled: Boolean

    )
}