package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL


/**
 * Kid Guardian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
data class KidGuardianDTO (

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: KidDTO? = null,

        /**
        * Guardian
        */
        @JsonProperty("guardian")
        var guardian: GuardianDTO? = null,

        /**
        * Is Confirmed
        */
        @JsonProperty("is_confirmed")
        var isConfirmed: Boolean = false,

        /**
        * Role
        */
        @JsonProperty("role")
        var role: String? = null,

        /**
        * Request At
        */
        @JsonProperty("request_at")
        var requestAt: String? = null

){}