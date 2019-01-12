package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Fun Time Scheduled DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class FunTimeScheduledDTO (

        /**
         * Enabled
         */
        @JsonProperty("enabled")
        var enabled: Boolean = false,

        /**
         * Monday
         */
        @JsonProperty("monday")
        var monday: DayScheduledDTO,

        /**
         * Tuesday
         */
        @JsonProperty("tuesday")
        var tuesday: DayScheduledDTO,

        /**
         * Wednesday
         */
        @JsonProperty("wednesday")
        var wednesday: DayScheduledDTO,

        /**
         * Thursday
         */
        @JsonProperty("thursday")
        var thursday: DayScheduledDTO,

        /**
         * Friday
         */
        @JsonProperty("friday")
        var friday: DayScheduledDTO,

        /**
         * Saturday
         */
        @JsonProperty("saturday")
        var saturday: DayScheduledDTO,

        /**
         * Sunday
         */
        @JsonProperty("sunday")
        var sunday: DayScheduledDTO
)