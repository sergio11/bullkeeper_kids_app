package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Add Message DTO
 */
data class AddMessageDTO(

        /**
         * Text
         */
        @JsonProperty("text")
        var text: String? = null,

        /**
         * From
         */
        @JsonProperty("from")
        var from: String? = null,

        /**
        * To
        */
        @JsonProperty("to")
        var to: String? = null

) {}