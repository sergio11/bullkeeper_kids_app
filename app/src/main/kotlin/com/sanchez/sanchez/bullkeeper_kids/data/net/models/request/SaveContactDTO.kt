package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Save Contact DTO
 */
data class SaveContactDTO (

        /**
         * Name
         */
        @get:JsonProperty("name")
        var name: String? = null,

        /**
         * Phone Number
         */
        @get:JsonProperty("phone_number")
        var phoneNumber: String? = null,

        /**
         * Local Id
         */
        @get:JsonProperty("local_id")
        var localId: String? = null,

        /**
         * Photo Encoded String
         */
        @get:JsonProperty("photo_encoded_string")
        var photoEncodedString: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null
)