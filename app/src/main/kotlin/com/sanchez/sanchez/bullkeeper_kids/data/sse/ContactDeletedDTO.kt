package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Rules DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ContactDeletedDTO(

        /**
         * Identity
         */
        @JsonProperty("id")
        val kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        val terminal: String,

        /**
         * Contact Id
         */
        @JsonProperty("contact_id")
        val contactId: String
)