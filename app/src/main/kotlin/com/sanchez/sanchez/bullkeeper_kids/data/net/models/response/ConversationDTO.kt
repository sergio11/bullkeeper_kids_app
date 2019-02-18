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
         * Last Message
         */
        @JsonProperty("last_message")
        var lastMessage: String? = null,

        /**
         * Last Message For Member One
         */
        @JsonProperty("last_message_for_member_one")
        var lastMessageForMemberOne: String? = null,

        /**
         * Last Message For Member Two
         */
        @JsonProperty("last_message_for_member_two")
        var lastMessageForMemberTwo: String? = null,

        /**
         * Last Message For Member One
         */
        @JsonProperty("pending_messages_for_member_one")
        var pendingMessagesForMemberOne: Long = 0,

        /**
         * Pending Messages For Member Two
         */
        @JsonProperty("pending_messages_for_member_two")
        var pendingMessagesForMemberTwo: Long = 0

)