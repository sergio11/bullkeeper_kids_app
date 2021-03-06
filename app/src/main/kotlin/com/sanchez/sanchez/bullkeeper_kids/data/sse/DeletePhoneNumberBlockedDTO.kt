package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Delete Phone Number Blocked DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeletePhoneNumberBlockedDTO(

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
         * Id Or Phone Number
         */
        @JsonProperty("id_or_phonenumber")
        var idOrPhonenumber: String

)