package com.sanchez.sanchez.bullkeeper_kids.domain.models

import java.io.Serializable
import java.util.*

/**
 * Guardian Entity
 */
data class GuardianEntity(

        /**
         * Identity
         */
         var identity: String? = null,

        /**
         * First Name
         */
        var firstName: String? = null,

        /**
         * Last Name
         */
        var lastName: String? = null,

        /**
         * Birth Date
         */
        var birthdate: Date? = null,

        /**
         * Age
         */

        var age: Int? = null,

        /**
         * Email
         */

        var email: String? = null,

        /**
         * Phone Prefix
         */
        var phonePrefix: String? = null,

        /**
        * Phone Number
         */

        var phoneNumber: String? = null,


        /**
         * Facebook Id
         */
        var fbId: String? = null,

        /**
         * Children
         */
        var children: Long? = null,

        /**
         * Locale
         */
        var locale: String? = null,

        /**
         * Profile Image
         */
        var profileImage: String? = null,

        /**
         * Visible
         */
        var visible: Boolean = false

): Serializable