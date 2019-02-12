package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Phone Number Blocked DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PhoneNumberBlockedDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Blocked At
         */
        @JsonProperty("blocked_at")
        var blockedAt: String? = null,

        /**
         * Prefix
         */
        @JsonProperty("prefix")
        var prefix: String? = null,

        /**
         * Number
         */
        @JsonProperty("number")
        var number: String? = null,

        /**
         * Phone Number
         */
        @JsonProperty("phonenumber")
        var phoneNumber: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: String? = null

)