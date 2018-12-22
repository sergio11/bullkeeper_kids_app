package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Phone Number Blocked Entity
 */
open class PhoneNumberBlockedEntity(

        /**
         * Identity
         */
        @PrimaryKey var identity: String? = "",

        /**
         * Blocked At
         */
        var blockedAt: String? = null,

        /**
         * Phone Number
         */
        var phoneNumber: String? = null

) : RealmObject(), Serializable