package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.fernandocejas.arrow.checks.Preconditions
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceEntity
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService.Companion.RECEIVE_GEOFENCE_ACTION
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGeofenceService
import timber.log.Timber
import javax.inject.Inject

/**
 * Geofence Service
 */
class DeviceGeofenceServiceImpl
    @Inject constructor(
            private var appContext: Context
    ): IDeviceGeofenceService {


    /**
     * Geofence Event Handler PI
     */
    private val geofenceEventHandlerPi: PendingIntent =
            PendingIntent.getBroadcast(appContext, 0,
                    Intent(RECEIVE_GEOFENCE_ACTION),
                    PendingIntent.FLAG_UPDATE_CURRENT)


    /**
     * Get Geofencing Client
     */
    private fun getGeofencingClient(): GeofencingClient = LocationServices
            .getGeofencingClient(appContext)


    /**
     * Add Geofence List
     */
    override fun addGeofence(geofenceEntityList: List<GeofenceEntity>) {
        for(geofence in geofenceEntityList)
            addGeofence(geofence)
    }

    /**
     * Add Geofence
     */
    @SuppressLint("MissingPermission")
    override fun addGeofence(geofenceEntity: GeofenceEntity) {

        val task = getGeofencingClient().addGeofences(
                GeofencingRequest.Builder().apply {
                    setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    addGeofence(Geofence.Builder().apply {
                        setRequestId(geofenceEntity.identity)
                        setCircularRegion(geofenceEntity.lat!!, geofenceEntity.log!!, geofenceEntity.radius!!)
                        setExpirationDuration(Geofence.NEVER_EXPIRE)
                        setNotificationResponsiveness(5000)
                        setTransitionTypes(when(geofenceEntity.transitionType) {
                            "TRANSITION_ENTER" -> Geofence.GEOFENCE_TRANSITION_ENTER
                            "TRANSITION_EXIT" -> Geofence.GEOFENCE_TRANSITION_EXIT
                            "TRANSITION_DWELL" -> Geofence.GEOFENCE_TRANSITION_DWELL
                            else -> Geofence.GEOFENCE_TRANSITION_EXIT
                        })
                    }.build())
                }.build(), geofenceEventHandlerPi).addOnSuccessListener {
                    Timber.d("GeofenceService: Geofence Created successfully")
                }
                .addOnFailureListener {
                    Timber.d("GeofenceService: Exception -> %s", it.message)
                }

        Tasks.await(task)
    }

    /**
     * Delete All Geofences
     */
    override fun deleteAllGeofences() {
        getGeofencingClient().removeGeofences(geofenceEventHandlerPi)
    }

    /**
     * Delete Geofence By Id
     */
    override fun deleteGeofenceById(identity: String) {
        Preconditions.checkNotNull(identity, "Identity can not be null")
        Preconditions.checkState(!identity.isEmpty(), "Identity can not be null or empty")
        getGeofencingClient().removeGeofences(arrayListOf(identity))
    }

    /**
     * Delete Geofences
     */
    override fun deleteGeofences(ids: List<String>) {
        Preconditions.checkNotNull(ids, "Ids can not be null")
        Preconditions.checkState(!ids.isEmpty(), "Ids can not be null or empty")
        getGeofencingClient().removeGeofences(ids)
    }
}