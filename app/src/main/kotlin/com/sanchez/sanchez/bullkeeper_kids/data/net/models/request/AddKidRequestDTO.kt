package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Add Kid Request
 */
data class AddKidRequestDTO (

        /**
         * Type
         */
        @get:JsonProperty("type")
        var type: String? = null,

        /**
         * location
         */
        @get:JsonProperty("location")
        var location: SaveCurrentLocationDTO? = null,

        /**
         * id
         */
        @get:JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal
         */
        @get:JsonProperty("terminal")
        var terminal: String? = null

)