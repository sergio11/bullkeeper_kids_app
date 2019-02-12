package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contact DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ContactDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,


        /**
         * Name
         */
        @JsonProperty("name")
        var name: String? = null,


        /**
         * Phone Number
         */
        @JsonProperty("phone_number")
        var phoneNumber: String? = null,


        /**
         * Local Id
         */
        @JsonProperty("local_id")
        var localId: String? = null,

        /**
         * Photo Encoded String
         */
        @JsonProperty("photo_encoded_string")
        var photoEncodedString: String? = null,

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null

)