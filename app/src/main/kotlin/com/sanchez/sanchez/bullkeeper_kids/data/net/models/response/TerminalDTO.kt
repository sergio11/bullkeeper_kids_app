package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Terminal DTO
 */
data class TerminalDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * App Version Name
         */
        @JsonProperty("app_version_name")
        var appVersionName: String? = null,

        /**
         * App Version Code
         */
        @JsonProperty("app_version_code")
        var appVersionCode: String? = null,

        /**
         * Os Version
         */
        @JsonProperty("os_version")
        var osVersion: String? = null,

        /**
         * SDK Version
         */
        @JsonProperty("sdk_version")
        var sdkVersion: String? = null,

        /**
         * Manufacturer
         */
        @JsonProperty("manufacturer")
        var manufacturer: String? = null,


        /**
         * Market Name
         */
        @JsonProperty("market_name")
        var marketName: String? = null,

        /**
         * Model
         */
        @JsonProperty("model")
        var model: String? = null,

        /**
         * Code Name
         */
        @JsonProperty("code_name")
        var codeName: String? = null,

        /**
         * Device Name
         */
        @JsonProperty("device_name")
        var deviceName: String? = null
)