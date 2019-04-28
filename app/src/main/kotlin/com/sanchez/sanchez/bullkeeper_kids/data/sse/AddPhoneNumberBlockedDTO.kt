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
        var identity: String,

        /**
         * Blocked At
         */
        @JsonProperty("blocked_at")
        var blockedAt: String,

        /**
         * Phone Number
         */
        @JsonProperty("prefix")
        var prefix: String? = null,


        /**
         * Number
         */
        @JsonProperty("number")
        var number: String,

        /**
         * Phone Number
         */
        @JsonProperty("phone_number")
        var phoneNumber: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String

)