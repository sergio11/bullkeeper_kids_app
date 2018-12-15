package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


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
        var callDayTime: Date? = null,

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
        var localId: String? = null
)