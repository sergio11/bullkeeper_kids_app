package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Proximity Event
 * https://code.tutsplus.com/es/tutorials/how-to-work-with-geofences-on-android--cms-26639
 */
open class GeofenceEntity(
        // Package Name
        @PrimaryKey var identity: String? = "",
        // Name
        var name: String? = "",
        // Latitude
        var lat: Double? = 0.0,
        // Longitude
        var log: Double? = 0.0,
        // Radius
        var radius: Float? = 0.0f,
        // Transition Type
        var transitionType: String? = TransitionTypeEnum.TRANSITION_ENTER.name,
        // Kid
        var kid: String? = null
) : RealmObject(), Serializable