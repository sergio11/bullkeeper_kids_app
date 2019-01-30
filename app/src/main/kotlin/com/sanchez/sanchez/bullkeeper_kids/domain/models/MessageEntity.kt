package com.sanchez.sanchez.bullkeeper_kids.domain.models

import java.util.*

/**
 * Message Entity
 */
data class MessageEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * Text
         */
        var text: String? = null,

        /**
         * Text
         */
        var createAt: Date? = null,

        /**
         * Conversation
         */
        var conversation: String? = null,

        /**
         * From
         */
        var from: PersonEntity? = null,


        /**
         * To
         */
        var to: PersonEntity? = null,

        /**
         * Viewed
         */
        var viewed: Boolean = false


) {
}