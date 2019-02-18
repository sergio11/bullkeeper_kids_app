package com.sanchez.sanchez.bullkeeper_kids.domain.models

import java.util.*

/**
 * Conversation Entity
 */
data class ConversationEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * Create At
         */
        var createAt: Date? = null,

        /**
         * Update At
         */
        var updateAt: Date? = null,

        /**
         * Member One
         */
        var memberOne: PersonEntity? = null,

        /**
         * Member Two
         */
        var memberTwo: PersonEntity? = null,

        /**
         * Message Count
         */
        var messagesCount: Long = 0,

        /**
         * Last Message
         */
        var lastMessage: String? = null,

        /**
         * Last Message For Member One
         */
        var lastMessageForMemberOne: String? = null,

        /**
         * Last Message For Member Two
         */
        var lastMessageForMemberTwo: String? = null,

        /**
         * Pending Messages For Member One
         */
        var pendingMessagesForMemberOne: Long = 0,

        /**
         * Pending Messages For Member Two
         */
        var pendingMessagesForMemberTwo: Long = 0


)