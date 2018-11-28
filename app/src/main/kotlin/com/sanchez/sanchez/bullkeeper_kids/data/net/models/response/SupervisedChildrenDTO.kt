package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Supervised Children DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SupervisedChildrenDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Role
         */
        @JsonProperty("role")
        var role: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: KidDTO? = null
)