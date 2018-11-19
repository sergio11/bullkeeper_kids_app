package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Save Terminal
 */
data class AddTerminalDTO (

        /**
         * App Version Code
         */
        @get:JsonProperty("app_version_code")
        var appVersionCode: String? = null,

        /**
         * App Version Name
         */
        @get:JsonProperty("app_version_name")
        var appVersionName: String? = null,

        /**
         * Code Name
         */
        @get:JsonProperty("code_name")
        var codeName: String? = null,

        /**
         * Device Name
         */
        @get:JsonProperty("device_name")
        var deviceName: String? = null,

        /**
         * Device Id
         */
        @get:JsonProperty("device_id")
        var deviceId: String? = null,

        /**
         * Manufacturer
         */
        @get:JsonProperty("manufacturer")
        var manufacturer: String? = null,

        /**
         * Market Name
         */
        @get:JsonProperty("market_name")
        var marketName: String? = null,

        /**
         * Model
         */
        @get:JsonProperty("model")
        var model: String? = null,

        /**
         * Os Version
         */
        @get:JsonProperty("os_version")
        var osVersion: String? = null,

        /**
         * SDK Version
         */
        @get:JsonProperty("sdk_version")
        var sdkVersion: String? = null,

        /**
         * Son Identity
         */
        @get:JsonProperty("son_id")
        var sonId: String? = null

)