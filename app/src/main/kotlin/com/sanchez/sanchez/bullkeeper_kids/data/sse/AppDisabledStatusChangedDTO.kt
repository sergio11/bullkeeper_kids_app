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
        @JsonProperty("kid")
        var kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String,


        /**
         * App
         */
        @JsonProperty("app")
        var app: String,


        /**
         * Disabled
         */
        @JsonProperty("disabled")
        var disabled: Boolean
)