package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Scheduled Block Status DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ScheduledBlockStatusDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String,

        /**
         * Enable
         */
        @JsonProperty("enable")
        var enable: Boolean
)