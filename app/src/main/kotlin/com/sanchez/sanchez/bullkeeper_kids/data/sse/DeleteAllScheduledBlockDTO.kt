package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Delete All Scheduled Block DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteAllScheduledBlockDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null
)