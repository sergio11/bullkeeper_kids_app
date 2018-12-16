package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty



/**
 * Save Call Detail DTO
 */
data class SaveCallDetailDTO (

        /**
         * Phone Number
         */
        @get:JsonProperty("phone_number")
        var phoneNumber: String? = null,

        /**
         * Call Day Time
         */
        @get:JsonProperty("call_day_time")
        var callDayTime: String? = null,

        /**
         * Call Duration
         */
        @get:JsonProperty("call_duration")
        var callDuration: String? = null,

        /**
         * Call Type
         */
        @get:JsonProperty("call_type")
        var callType: String? = null,

        /**
         * Local Id
         */
        @get:JsonProperty("local_id")
        var localId: String? = null,

        /**
         * Kid
         */
        @get:JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal
         */
        @get:JsonProperty("terminal")
        var terminal: String? = null
)