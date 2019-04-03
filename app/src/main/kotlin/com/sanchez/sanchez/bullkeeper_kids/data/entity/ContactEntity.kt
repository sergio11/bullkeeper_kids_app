package com.sanchez.sanchez.bullkeeper_kids.data.entity

import com.sanchez.sanchez.bullkeeper_kids.core.extension.toDateTime
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.Date

/**
 * Email Contact
 */
data class EmailContact(val email: String)

/**
 * Phone Contact
 */
data class PhoneContact(val phone: String)

/**
 * Postal Address
 */
data class PostalAddress(val city: String, val state: String, val country: String)

/**
 * Contact Entity
 */
open class ContactEntity(
        // Id
        @PrimaryKey var id: String? = "",
        // Name
        var name: String? = "",
        // Photo Encoded String
        var photoEncodedString: String?  = "",
        // Last Update Timestap
        var lastUpdateTimestamp: String? = "",
        // Phone List
        @Ignore
        var phoneList: ArrayList<PhoneContact> = ArrayList(),
        // Email List
        @Ignore
        var emailList: ArrayList<EmailContact> = ArrayList(),
        // Address List
        @Ignore
        var addressList: ArrayList<PostalAddress> = ArrayList(),
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