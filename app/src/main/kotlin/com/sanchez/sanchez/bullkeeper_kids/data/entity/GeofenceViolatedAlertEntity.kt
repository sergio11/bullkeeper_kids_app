package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Geofence Violated Alert Entity
 */
open class GeofenceViolatedAlertEntity(
        @PrimaryKey var timestamp: Long? = 0L,
        // Kid
        var kid: String? = "",
        // Identity
        var geofence: String? = "",
        // Transition Type
        var transitionType: String? = "",
        // Terminal
        var terminal: String? = ""
) : RealmObject(), Serializable