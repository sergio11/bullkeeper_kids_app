package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Conversation DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ConversationDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: String? = null,

        /**
        * Update At
        */
        @JsonProperty("update_at")
        var updateAt: String? = null,

        /**
         * Member One
         */
        @JsonProperty("member_one")
        var memberOne: PersonDTO? = null,

        /**
         * Member Two
         */
        @JsonProperty("member_two")
        var memberTwo: PersonDTO? = null,

        /**
        * Messages Count
        */
        @JsonProperty("messages_count")
        var messagesCount: Long = 0,

        /**
         * Unread Messages
         */
        @JsonProperty("unread_messages")
        var unreadMessages: Long = 0,

        /**
         * Last Message
         */
        @JsonProperty("last_message")
        var lastMessage: String? = null

)