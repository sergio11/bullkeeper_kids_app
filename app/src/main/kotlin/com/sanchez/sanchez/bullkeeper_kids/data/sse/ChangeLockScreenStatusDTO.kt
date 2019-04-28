package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Change Lock Screen Status DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChangeLockScreenStatusDTO(

        /**
         * Identity
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String,

        /**
         * Is Enabled
         */
        @JsonProperty("is_enabled")
        var enabled: Boolean
)