package com.sanchez.sanchez.bullkeeper_kids.presentation.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.presentation.home.HomeActivity
import timber.log.Timber

/**
 * Geofence Transition Service
 */
class GeofenceTransitionService: IntentService("GeofenceTransitionService") {

    val GEOFENCE_NOTIFICATION_ID = 0

    /**
     * On Handle Intent
     */
    override fun onHandleIntent(intent: Intent?) {
        Timber.d("Geofence Transition Service executed")
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
            Timber.d("Geofence Transition Type -> %d", geoFenceTransition)
            // Get the geofence that were triggered
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            // Create a detail message with Geofences received
            val geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences)
            // Send notification details as a String
            sendNotification(geofenceTransitionDetails)
        }
    }

    // Create a detail message with Geofences received
    private fun getGeofenceTrasitionDetails(geoFenceTransition: Int, triggeringGeofences: List<Geofence>): String {
        // get the ID of each geofence triggered
        val triggeringGeofencesList = ArrayList<String>()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesList.add(geofence.requestId)
        }

        var status: String? = null
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "Entering "
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "Exiting "
        return status!! + TextUtils.join(", ", triggeringGeofencesList)
    }

    // Send a notification
    private fun sendNotification(msg: String) {
        // Intent to start the main Activity
        /*val notificationIntent = HomeActivity.makeNotificationIntent(
                applicationContext, msg
        )*/

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(HomeActivity::class.java)
        //stackBuilder.addNextIntent(notificationIntent)
        val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        // Creating and sending Notification
        val notificatioMng = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent))
    }

    // Create a notification
    private fun createNotification(msg: String, notificationPendingIntent: PendingIntent):
            Notification {
        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS or
                        Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
        return notificationBuilder.build()
    }

    // Handle errors
    private fun getErrorString(errorCode: Int): String {
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "GeoFence not available"
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "Too many GeoFences"
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "Too many pending intents"
            else -> "Unknown error."
        }
    }
}