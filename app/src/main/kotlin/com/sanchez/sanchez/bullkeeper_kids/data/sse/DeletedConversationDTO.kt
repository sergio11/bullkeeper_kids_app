package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Deleted Conversation DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeletedConversationDTO(

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String,

        /**
         * Member One
         */
        @JsonProperty("member_one")
        var memberOne: String,

        /**
         * Member Two
         */
        @JsonProperty("member_two")
        var memberTwo: String
)