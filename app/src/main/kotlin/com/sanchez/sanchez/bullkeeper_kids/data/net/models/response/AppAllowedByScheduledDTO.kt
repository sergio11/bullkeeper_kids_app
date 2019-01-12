package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Allowed By Scheduled DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppAllowedByScheduledDTO(

        /**
         * App Installed
         */
        @JsonProperty("app")
        var app: AppInstalledDTO? = null,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        var terminal: TerminalDTO? = null
)