package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Call Detail DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CallDetailDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Local Id
         */
        @JsonProperty("local_id")
        var localId: String? = null


)