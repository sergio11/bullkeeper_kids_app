package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Delete All Phone Number Blocked DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteAllPhoneNumberBlockedDTO(

        /**
         * Identity
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null

)