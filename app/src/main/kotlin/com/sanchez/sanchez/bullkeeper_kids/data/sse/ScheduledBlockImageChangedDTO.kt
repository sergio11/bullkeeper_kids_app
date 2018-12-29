package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Scheduled Block Image Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ScheduledBlockImageChangedDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Block
         */
        @JsonProperty("block")
        var block: String? = null,

        /**
         * Image
         */
        @JsonProperty("image")
        var image: String? = null
)