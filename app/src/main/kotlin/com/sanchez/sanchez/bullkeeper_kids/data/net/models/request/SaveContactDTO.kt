package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Email Contact
 */
data class EmailContactDTO(
        /**
         * Email
         */
        @get:JsonProperty("email")
        val email: String
)

/**
 * Phone Contact
 */
data class PhoneContactDTO(
        /**
         * Phone
         */
        @get:JsonProperty("phone")
        val phone: String
)

/**
 * Postal Address
 */
data class PostalAddressDTO(

        /**
         * Phone
         */
        @get:JsonProperty("city")
        val city: String,

        /**
         * State
         */
        @get:JsonProperty("state")
        val state: String,

        /**
         * Country
         */
        @get:JsonProperty("country")
        val country: String

)

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
         * Phone List
         */
        @get:JsonProperty("phone_list")
        var phoneList: List<PhoneContactDTO> = ArrayList(),

        /**
         * Email List
         */
        @get:JsonProperty("email_list")
        var emailList: List<EmailContactDTO> = ArrayList(),

        /**
         * Address List
         */
        @get:JsonProperty("address_list")
        var addressList: List<PostalAddressDTO> = ArrayList(),

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