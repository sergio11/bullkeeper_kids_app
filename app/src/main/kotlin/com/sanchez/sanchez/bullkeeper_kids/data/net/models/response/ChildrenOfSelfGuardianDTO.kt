package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Children Of Self Guardian DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChildrenOfSelfGuardianDTO(

        /**
         * Identity
         */
        @JsonProperty("total")
        var total: Long? = null,

        /**
         * Confirmed
         */
        @JsonProperty("confirmed")
        var confirmed: Long? = null,

        /**
         * No Confirmed
         */
        @JsonProperty("no_confirmed")
        var noConfirmed: Long? = null,

        /**
         * Supervised ChildrenS
         */
        @JsonProperty("supervised_children")
        var supervisedChildrenList: List<SupervisedChildrenDTO>? = null
)