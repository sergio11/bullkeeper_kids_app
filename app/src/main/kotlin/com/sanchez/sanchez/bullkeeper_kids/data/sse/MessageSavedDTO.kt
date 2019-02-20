package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.PersonDTO
import java.io.Serializable

/**
 * Message Saved DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageSavedDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String,

        /**
         * Text
         */
        @JsonProperty("text")
        var text: String,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: String,

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String,


        /**
         * From
         */
        @JsonProperty("from")
        var from: PersonDTO,

        /**
         * To
         */
        @JsonProperty("to")
        var to: PersonDTO,

        /**
         * Viewed
         */
        @JsonProperty("viewed")
        var viewed: Boolean
): Serializable