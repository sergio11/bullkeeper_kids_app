package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Day Scheduled DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DayScheduledDTO (
        /**
         * Day
         */
        @JsonProperty("day")
        var day: String? = null,

        /**
         * Enabled
         */
        @JsonProperty("enabled")
        var enabled: Boolean = false,

        /**
         * Total Hours
         */
        @JsonProperty("total_hours")
        var totalHours: Long = 0

)