package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Terminal Screen Status Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TerminalScreenStatusChangedDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,

        /**
         * Enabled
         */
        @JsonProperty("enabled")
        var enabled: Boolean? = null

)