package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Fun Time Day Scheduled Entity
 */
open class FunTimeDayScheduledEntity(
        // Day
        @PrimaryKey var day: String? = "",
        // Enabled
        var enabled: Boolean = false,
        // Total Hours
        var totalHours: Int = 0,
        // Paused
        var paused: Boolean = false,
        // Paused At
        var pausedAt: String? = null
) : RealmObject(), Serializable