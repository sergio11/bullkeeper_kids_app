package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Rule DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppRuleDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,


        /**
         * Package Name
         */
        @JsonProperty("package_name")
        var  packageName: String? = null,

        /**
         * App Rule
         */
        @JsonProperty("app_rule")
        var appRule: String? = null
)