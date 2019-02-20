package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Deleted Message DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeletedMessagesDTO(

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String,

        /**
         * ids
         */
        @JsonProperty("ids")
        var ids: List<String>
): Serializable