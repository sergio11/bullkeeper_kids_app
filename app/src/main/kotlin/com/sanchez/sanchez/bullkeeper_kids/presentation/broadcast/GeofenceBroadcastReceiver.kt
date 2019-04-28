package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import timber.log.Timber
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.GeofenceComponent
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.Geofence
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.functional.map
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GeofenceViolatedAlertEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IGeofenceViolatedAlertRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.SaveGeofenceAlertInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import java.util.*
import javax.inject.Inject


/**
 * Geofence Broadcast Receiver
 */
class GeofenceBroadcastReceiver : BroadcastReceiver()  {

    /**
     * Geofence Component
     */
    private val geofenceComponent: GeofenceComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.geofenceComponent
    }

    /**
     * Dependencies
     *
     * ================
     */

    /**
     * Geofence Repository
     */
    @Inject
    internal lateinit var geofenceRepository: IGeofenceRepository

    /**
     * Save Geofence Alert Interact
     */
    @Inject
    internal lateinit var saveGeofenceAlertInteract: SaveGeofenceAlertInteract

    /**
     * Geofence Violated Alert Repository
     */
    @Inject
    internal lateinit var geofenceViolatedAlertRepository: IGeofenceViolatedAlertRepository

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Local Notification Service
     */
    @Inject
    internal lateinit var localNotificationService: ILocalNotificationService


    /**
     * State
     * ============
     *
     */

    private lateinit var context: Context

    private val notificationId: Int = 12357


    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("GeofenceReceiver: Started")
        geofenceComponent.inject(this)
        this.context = context
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            handleError(intent)
        } else {
            handleEnterExit(geofencingEvent)
        }
    }


    /**
     * Private Methods
     * ===================
     */


    /**
     * Handler Error
     */
    private fun handleError(intent: Intent?) {
        Timber.d("GeofenceReceiver: Handle Error")
        // Get the error code
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val errorMessage = getErrorString(context,
                geofencingEvent.errorCode)
        Timber.d("GeofenceReceiver: Error Message -> %s", errorMessage)

    }

    /**
     * Handle Enter/Exit transitions
     */
    private fun handleEnterExit(geofencingEvent: GeofencingEvent) {

        Timber.d("GeofenceReceiver: Handle Enter/Exit transition")

        // Get the type of transition (entry or exit)
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that a valid transition was reported
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            geofenceRepository
                    .findByIds(triggeringGeofences.map { it.requestId })
                    .filter { it.isEnabled!! }.let { triggeringGeofencesList ->

                        if(triggeringGeofencesList.isNotEmpty()) {

                            triggeringGeofencesList.last().let { lastGeofence ->

                                navigator.showGeofenceViolatedActivity(context, lastGeofence.name, lastGeofence.transitionType,
                                        lastGeofence.radius)

                                localNotificationService.sendNotification(ILocalNotificationService.NotificationTypeEnum.IMPORTANT, notificationId,
                                        lastGeofence.name!!, when(lastGeofence.transitionType) {
                                            "TRANSITION_ENTER" -> context.getString(R.string.geofence_transition_enter_description)
                                            "TRANSITION_EXIT" -> context.getString(R.string.geofence_transition_exit_description)
                                            else -> context.getString(R.string.geofence_transition_exit_description)
                                        })
                            }

                            geofenceViolatedAlertRepository.save(triggeringGeofencesList.map {
                                GeofenceViolatedAlertEntity(
                                        timestamp = Date().time,
                                        kid = it.kid,
                                        geofence = it.identity,
                                        transitionType = it.transitionType,
                                        terminal = preferenceRepository.getPrefTerminalIdentity()
                                )
                            }.toList())


                            saveGeofenceAlertInteract(SaveGeofenceAlertInteract.Params(preferenceRepository.getPrefKidIdentity(),
                                    triggeringGeofencesList.map { geofenceEntity ->  SaveGeofenceAlertInteract.GeofenceViolatedAlert(geofenceEntity.kid!!, geofenceEntity.identity!!,
                                            geofenceEntity.transitionType!!, preferenceRepository.getPrefTerminalIdentity()) })){
                                it.either(fun(_: Failure){
                                    Timber.d("Save Geofence Alert Failed")

                                    geofenceViolatedAlertRepository.save(triggeringGeofencesList.map {
                                        GeofenceViolatedAlertEntity(
                                                timestamp = Date().time,
                                                kid = it.kid,
                                                geofence = it.identity,
                                                transitionType = it.transitionType,
                                                terminal = preferenceRepository.getPrefTerminalIdentity()
                                        )
                                    }.toList())

                                }, fun(_: Unit){
                                    Timber.d("Save Geofence Alert Success")
                                })
                            }

                        }
            }
        } else {
            Timber.e(context.getString(R.string.geofence_transition_invalid_type,
                    geofenceTransition))
        }
    }


    /**
     * Returns the error string for a geofencing error code.
     */
    private fun getErrorString(context: Context, errorCode: Int): String {
        val mResources = context.resources
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> mResources.getString(R.string.geofence_not_available)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> mResources.getString(R.string.geofence_too_many_geofences)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> mResources.getString(R.string.geofence_too_many_pending_intents)
            else -> mResources.getString(R.string.unknown_geofence_error)
        }
    }

}