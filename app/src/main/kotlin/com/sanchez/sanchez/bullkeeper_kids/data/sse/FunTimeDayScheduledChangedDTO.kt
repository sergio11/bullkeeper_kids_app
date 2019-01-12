package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Fun Time Day Scheduled Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class FunTimeDayScheduledChangedDTO(

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