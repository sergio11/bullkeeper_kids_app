package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Delete Scheduled Block DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteScheduledBlockDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Block
         */
        @JsonProperty("block")
        var block: String? = null
)