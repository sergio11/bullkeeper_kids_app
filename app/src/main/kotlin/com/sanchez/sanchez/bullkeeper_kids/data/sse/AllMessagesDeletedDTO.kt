package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * All Messages Deleted DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AllMessagesDeletedDTO(

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String
): Serializable