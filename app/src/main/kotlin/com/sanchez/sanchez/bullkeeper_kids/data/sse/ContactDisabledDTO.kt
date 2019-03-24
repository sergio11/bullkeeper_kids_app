package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contact Disable DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ContactDisabledDTO(

        /**
         * Identity
         */
        @JsonProperty("kid")
        val kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        val terminal: String,

        /**
         * Contact
         */
        @JsonProperty("contact")
        val contact: String,

        /**
         * Local Id
         */
        @JsonProperty("local_id")
        val localId: String
)