package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Call Detail Entity
 */
open class CallDetailEntity(
        // Id
        @PrimaryKey var id: String? = "",
        // Phone Number
        var phoneNumber: String? = null,
        // Call Day Time
        var callDayTime: Date? = null,
        // Call Duration
        var callDuration: String? = null,
        // Call Type
        var callType: String? = null,
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = ""
) : RealmObject(), Serializable