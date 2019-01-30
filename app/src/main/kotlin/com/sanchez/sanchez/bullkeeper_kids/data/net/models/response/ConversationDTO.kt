package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

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
        var createAt: Date? = null,

        /**
        * Update At
        */
        @JsonProperty("update_at")
        var updateAt: Date? = null,


        /**
         * Kid Guardian
         */
        @JsonProperty("kid_guardian")
        var kidGuardian: KidGuardianDTO? = null,


        /**
        * Messages Count
        */
        @JsonProperty("messages_count")
        var messagesCount: Long = 0

)