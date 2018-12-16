package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

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
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = ""
) : RealmObject(), Serializable