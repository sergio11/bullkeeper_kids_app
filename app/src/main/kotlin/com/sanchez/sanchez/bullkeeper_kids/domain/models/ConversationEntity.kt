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
        var messagesCount: Long = 0

)