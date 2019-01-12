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
        var totalHours: Int = 0,

        /**
         * Paused
         */
        @JsonProperty("paused")
        var paused: Boolean = false,

        /**
         * Paused At
         */
        @JsonProperty("paused_at")
        var pausedAt: String? = null
)