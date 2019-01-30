package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Message DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageDTO (

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Text
         */
        @JsonProperty("text")
        var text: String? = null,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: Date? = null,

        /**
         * Conversation
         */
        @JsonProperty("conversation")
        var conversation: String? = null,

        /**
         * Viewed
         */
        @JsonProperty("viewed")
        var viewed: Boolean = false,

        /**
         * From
         */
        @JsonProperty("from")
        var from: PersonDTO? = null,

        /**
         * To
         */
        @JsonProperty("to")
        var to: PersonDTO? = null
)