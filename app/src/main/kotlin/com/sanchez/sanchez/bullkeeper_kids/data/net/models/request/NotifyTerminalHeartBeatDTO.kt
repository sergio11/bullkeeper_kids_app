package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Notify Terminal HeartBeat DTO
 */
data class NotifyTerminalHeartBeatDTO (

        /**
         * Kid
         */
        @get:JsonProperty("kid")
        var kid: String? = null,

        /**
         * Screen Status
         */
        @get:JsonProperty("screen_status")
        var screenStatus: String? = null,

        /**
         * Terminal
         */
        @get:JsonProperty("terminal")
        var terminal: String? = null

)