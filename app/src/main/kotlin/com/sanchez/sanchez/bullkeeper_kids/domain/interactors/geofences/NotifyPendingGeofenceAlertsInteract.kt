package com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences

import android.annotation.SuppressLint
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveGeofenceAlertDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IGeofencesService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceViolatedAlertRepository
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

/**
 * Notify Pending Geofence Alerts Interact
 */
class NotifyPendingGeofenceAlertsInteract
    @Inject constructor(
            private val geofencesService: IGeofencesService,
            private val geofenceViolatedAlertRepository: IGeofenceViolatedAlertRepository,
            retrofit: Retrofit
            ): UseCase<Unit, NotifyPendingGeofenceAlertsInteract.Params>(retrofit) {

    /**
     *
     */
    @SuppressLint("MissingPermission")
    override suspend fun onExecuted(params: Params) {

        geofenceViolatedAlertRepository.findByKid(params.kid)?.sortedBy { it.timestamp }?.map {
            SaveGeofenceAlertDTO(it.kid, it.geofence,
                    it.transitionType, it.terminal)
        }?.let {
            val response =
                    geofencesService.saveGeofenceAlerts(params.kid, it ).await()

            response.httpStatus?.let {
                if (it == "OK") {
                    Timber.d("Delete Pending Geofence Alerts")
                    geofenceViolatedAlertRepository.deleteByKid(params.kid)
                }
            }
        }
    }

    /**
     * Params
     */
    data class Params(
            // Kid
            var kid: String
    )
}