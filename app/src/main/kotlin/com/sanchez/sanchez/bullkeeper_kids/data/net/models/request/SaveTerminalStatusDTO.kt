package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Save Terminal Status DTO
 */
data class SaveTerminalStatusDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: String,

        /**
         * Status
         */
        @JsonProperty("status")
        var status: String

)