package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Save SMS DTO
 */
data class SaveSmsDTO (

        /**
         * Address
         */
        @get:JsonProperty("address")
        var address: String? = null,

        /**
         * Message
         */
        @get:JsonProperty("message")
        var message: String? = null,

        /**
         * Read State
         */
        @get:JsonProperty("read_state")
        var readState: String? = null,

        /**
         * Time
         */
        @get:JsonProperty("date")
        var time: String? = null,

        /**
         * Folder Name
         */
        @get:JsonProperty("folder_name")
        var folderName: String? = null,

        /**
         * Local Id
         */
        @get:JsonProperty("local_id")
        var localId: String? = null

)