package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Sms Entity
 */
open class SmsEntity(
        // Id
        @PrimaryKey var id: String? = "",
        // Address
        var address: String? = "",
        // Message
        var message: String?  = "",
        // Read State
        var readState: String?  = "",
        // Folder Name
        var folderName: String? = "",
        // Time
        var time: String? = "",
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = "",
        // Remove
        var remove: Int = 0

) : RealmObject(), Serializable