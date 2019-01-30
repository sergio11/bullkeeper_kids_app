package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Guardian DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GuardianDTO(


        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * First Name
         */
        @JsonProperty("first_name")
        var firstName: String? = null,

        /**
         * Last Name
         */
        @JsonProperty("last_name")
        var lastName: String? = null,

        /**
         * Birthdate
         */
        @JsonProperty("birthdate")
        var birthdate: Date? = null,

        /**
         * Age
         */
        @JsonProperty("age")
        var age: Int? = null,

        /**
         * Email
         */
        @JsonProperty("email")
        var email: String? = null,

        /**
         * Phone Prefix
         */
        @JsonProperty("phone_prefix")
        var phonePrefix: String? = null,

        /**
         * Phone Number
         */
        @JsonProperty("phone_number")
        var phoneNumber: String? = null,

        /**
         * FB
         */
        @JsonProperty("fb_id")
        var fbId: String? = null,

        /**
         * Children
         */
        @JsonProperty("children")
        var children: Long? = null,

        /**
         * Locale
         */
        @JsonProperty("locale")
        var locale: String? = null,

        /**
         * Profile Image
         */
        @JsonProperty("profile_image")
        var profileImage: String? = null,

        /**
         * Visible
         */
        @JsonProperty("visible")
        var visible: Boolean = false

)