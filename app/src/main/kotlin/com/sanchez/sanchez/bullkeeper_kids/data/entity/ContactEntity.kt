package com.sanchez.sanchez.bullkeeper_kids.data.entity

import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTime
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Contact Entity
 */
open class ContactEntity(
        // Id
        @PrimaryKey var id: String? = "",
        // Name
        var name: String? = "",
        // Phone Number
        var phoneNumber: String?  = "",
        // Photo Encoded String
        var photoEncodedString: String?  = "",
        // Last Update Timestap
        var lastUpdateTimestamp: String? = "",
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = "",
        // Require remove
        var remove: Int = 0
) : RealmObject(), Serializable {

    /**
     * Get Last Update
     */
    fun getLastUpdateAsDate(): Date? {
        return lastUpdateTimestamp?.toLong()
                ?.toDateTime()
    }

}