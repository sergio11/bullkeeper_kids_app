package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * App Allowed By Scheduled Entity
 */
open class AppAllowedByScheduledEntity(
        // Identity
        @PrimaryKey
        var identity: String? = null,
        // Kid
        var app: String? = null,
        // Scheduled Block
        var scheduledBlock: String? = null
): RealmObject(), Serializable {}