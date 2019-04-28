package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Rules DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppRulesDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String,


        /**
         * Type
         */
        @JsonProperty("type")
        var type: String
)