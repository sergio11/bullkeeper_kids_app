package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * SMS DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SmsDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,


        /**
         * Address
         */
        @JsonProperty("address")
        var address: String? = null,


        /**
         * Message
         */
        @JsonProperty("message")
        var message: String? = null,


        /**
         * Read State
         */
        @JsonProperty("read_state")
        var readState: String? = null,


        /**
         * Date
         */
        @JsonProperty("date")
        var date: String? = null,


        /**
         * Folder Name
         */
        @JsonProperty("folder_name")
        var folderName: String? = null,


        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String? = null,

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: String? = null,

        /**
         * Local Id
         */
        @JsonProperty("local_id")
        var localId: String? = null

)