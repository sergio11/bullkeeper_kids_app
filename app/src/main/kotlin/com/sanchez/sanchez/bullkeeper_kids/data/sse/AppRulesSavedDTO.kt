package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Rules Saved DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppRulesSavedDTO(

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
         * Type
         */
        @JsonProperty("type")
        var type: String

)