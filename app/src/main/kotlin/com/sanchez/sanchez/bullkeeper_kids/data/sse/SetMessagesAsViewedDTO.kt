package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Set Messages As Viewed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SetMessagesAsViewedDTO(

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String,

        /**
         * Message Ids
         */
        @JsonProperty("messageIds")
        var messageIds: List<String>
): Serializable