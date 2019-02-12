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
        @JsonProperty("id")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,


        /**
         * App Rules List
         */
        @JsonProperty("app_rules")
        var appRulesList: Iterable<AppRulesDTO>

)