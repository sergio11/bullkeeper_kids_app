package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * All Conversation Deleted DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AllConversationDeletedDTO(

        /**
         * Member
         */
        @JsonProperty("member")
        var member: String
): Serializable