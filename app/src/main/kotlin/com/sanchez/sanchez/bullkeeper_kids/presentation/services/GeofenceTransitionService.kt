package com.sanchez.sanchez.bullkeeper_kids.presentation.services

import android.app.*
import android.content.Intent
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.GeofenceComponent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import timber.log.Timber
import javax.inject.Inject

/**
 * Geofence Transition Service
 */
class GeofenceTransitionService: IntentService("GeofenceTransitionService") {


    /**
     * Geofence Component
     */
    private val geofenceComponent: GeofenceComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.geofenceComponent
    }

    /**
     * Local Notification Service
     */
    @Inject
    internal lateinit var localNotificationService: ILocalNotificationService

    /**
     * Geofence Repository
     */
    @Inject
    internal lateinit var geofenceRepository: IGeofenceRepository

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator


    val GEOFENCE_NOTIFICATION_ID = 0


    /**
     * On Handle Intent
     */
    override fun onHandleIntent(intent: Intent?) {
        geofenceComponent.inject(this)
        Timber.d("GTS: Geofence Transition Service executed")
        // Retrieve the Geofencing intent
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        // Handling errors
        if (geofencingEvent.hasError()) {
            val errorMsg = getErrorString(geofencingEvent.errorCode)
            return
        }
        // Retrieve GeofenceTrasition
        val geoFenceTransition = geofencingEvent.geofenceTransition
        // Check if the transition type
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofence that were triggered
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            // Check Geofences
            checkGeofenceDetails(geoFenceTransition, triggeringGeofences)
        }
    }

    /**
     * Check Geofence Transition Details
     */
    private fun checkGeofenceDetails(geoFenceTransition: Int,
                                   triggeringGeofences: List<Geofence>) {

        // get the ID of each geofence triggered
        val triggeringGeofencesList = ArrayList<String>()

        for (geofence in triggeringGeofences) {
            triggeringGeofencesList.add(geofence.requestId)
        }

        // Get Geofences Details
        val geofence = geofenceRepository
                .findByIds(triggeringGeofencesList).firstOrNull()

        geofence?.let {

            navigator.showGeofenceViolatedActivity(this, it.name, it.transitionType,
                    it.radius)

        }
    }

    /**
     * Get Error String
     */
    private fun getErrorString(errorCode: Int): String {
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "GeoFence not available"
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "Too many GeoFences"
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "Too many pending intents"
            else -> "Unknown error."
        }
    }
}