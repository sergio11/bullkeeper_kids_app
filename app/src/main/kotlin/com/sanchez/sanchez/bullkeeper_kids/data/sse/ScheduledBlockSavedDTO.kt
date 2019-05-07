package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.AppAllowedByScheduledDTO
import org.joda.time.LocalTime

/**
 * Scheduled Block Saved DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ScheduledBlockSavedDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String,

        /**
         * Name
         */
        @JsonProperty("name")
        var name: String,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: String,

        /**
         * Enable
         */
        @JsonProperty("enable")
        var enable: Boolean = false,

        /**
         * Repeatable
         */
        @JsonProperty("repeatable")
        var repeatable: Boolean = false,

        /**
         * Allow Calls
         */
        @JsonProperty("allow_calls")
        var allowCalls: Boolean = false,

        /**
         * Description
         */
        @JsonProperty("description")
        var description: String,

        /**
         * Image
         */
        @JsonProperty("image")
        var image: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Start At
         */
        @JsonProperty("start_at")
        var startAt: LocalTime,

        /**
         * End At
         */
        @JsonProperty("end_at")
        var endAt: LocalTime,

        /**
         * Apps Allowed
         */
        @JsonProperty("apps_allowed")
        var appsAllowed: List<AppAllowedByScheduledDTO>? = null,

        /**
         * Weekly Frequency
         */
        @JsonProperty("weekly_frequency")
        var weeklyFrequency: IntArray)
{}