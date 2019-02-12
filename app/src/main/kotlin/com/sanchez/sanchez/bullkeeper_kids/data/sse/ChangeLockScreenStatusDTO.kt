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
        @JsonProperty("id")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,

        /**
         * Is Enabled
         */
        @JsonProperty("is_enabled")
        var enabled: Boolean? = null
)