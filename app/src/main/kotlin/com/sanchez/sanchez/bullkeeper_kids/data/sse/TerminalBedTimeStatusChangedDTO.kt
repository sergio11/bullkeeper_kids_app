package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Terminal Bed Time Status Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TerminalBedTimeStatusChangedDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String,

        /**
         * Enabled
         */
        @JsonProperty("enabled")
        var enabled: Boolean


)