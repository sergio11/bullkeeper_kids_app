package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
        var identity: String? = null,

        /**
         * Name
         */
        @JsonProperty("name")
        var name: String? = null,

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
        var description: String? = null,

        /**
         * Image
         */
        @JsonProperty("image")
        var image: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Start At
         */
        @JsonProperty("start_at")
        var startAt: LocalTime? = null,

        /**
         * End At
         */
        @JsonProperty("end_at")
        var endAt: LocalTime? = null,

        /**
         * Weekly Frequency
         */
        @JsonProperty("weekly_frequency")
        var weeklyFrequency: IntArray? = null)
{}