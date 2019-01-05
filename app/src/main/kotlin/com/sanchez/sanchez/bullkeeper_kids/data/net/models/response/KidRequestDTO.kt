package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Kid Request DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class KidRequestDTO(


        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Type
         */
        @JsonProperty("type")
        var type: String? = null,

        /**
         * Request At
         */
        @JsonProperty("request_at")
        var requestAt: Date? = null,

        /**
         * Expired At
         */
        @JsonProperty("expired_at")
        var expiredAt: Date? = null,

        /**
         * Location
         */
        @JsonProperty("location")
        var location: LocationDTO? = null,


        /**
         * Since
         */
        @JsonProperty("since")
        var since: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: KidDTO? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: TerminalDTO? = null

)