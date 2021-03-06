package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Rules List Saved DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppRulesListSavedDTO(

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
         * App Rules List
         */
        @JsonProperty("app_rules")
        var appRulesList: Iterable<AppRulesDTO>

)