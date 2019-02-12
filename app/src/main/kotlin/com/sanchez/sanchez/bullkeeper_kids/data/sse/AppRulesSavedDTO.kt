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
         * Type
         */
        @JsonProperty("type")
        var type: String? = null

)