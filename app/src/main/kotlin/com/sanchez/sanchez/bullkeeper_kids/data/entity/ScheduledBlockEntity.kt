package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Scheduled Block Entity
 */
open class ScheduledBlockEntity(
        // Id
        @PrimaryKey var id: String? = "",
        // Name
        var name: String? = null,
        // Enable
        var enable: Boolean = false,
        // Repeatable
        var repeatable: Boolean = false,
        // Create At
        var createAt: String? = null,
        // Image
        var image: String? = null,
        // Kid
        var kid: String? = null,
        // Start At
        var startAt: String? = null,
        // End At
        var endAt: String? = null,
        // Weekly Frequency
        var weeklyFrequency: String? = null,
        // Allow Calls
        var allowCalls: Boolean? = null,
        // Description
        var description: String? = null
) : RealmObject(), Serializable