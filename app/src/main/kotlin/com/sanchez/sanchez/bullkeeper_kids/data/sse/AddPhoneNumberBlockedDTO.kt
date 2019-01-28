package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Add Phone Number Blocked DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AddPhoneNumberBlockedDTO(

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
         * Phone Number
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
        @JsonProperty("phone_number")
        var phoneNumber: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null

)