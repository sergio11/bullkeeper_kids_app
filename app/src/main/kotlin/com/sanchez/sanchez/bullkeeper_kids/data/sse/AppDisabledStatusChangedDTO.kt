package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Disabled Status Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppDisabledStatusChangedDTO(

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
         * App
         */
        @JsonProperty("app")
        var app: String? = null,


        /**
         * Disabled
         */
        @JsonProperty("disabled")
        var disabled: Boolean? = null
)