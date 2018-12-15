package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Save App Installed DTO
 */
data class SaveAppInstalledDTO (
        /**
         * Package Name
         */
        @get:JsonProperty("package_name")
        var packageName: String? = null,

        /**
         * First Install Time
         */
        @get:JsonProperty("first_install_time")
        var firstInstallTime: Long? = null,

        /**
         * Last Update Time
         */
        @get:JsonProperty("last_update_time")
        var lastUpdateTime: Long? = null,

        /**
         * Version Name
         */
        @get:JsonProperty("version_name")
        var versionName: String? = null,

        /**
         * Version Code
         */
        @get:JsonProperty("version_code")
        var versionCode: String? = null,

        /**
         * App Name
         */
        @get:JsonProperty("app_name")
        var appName: String? = null,

        /**
         * App Rule
         */
        @get:JsonProperty("app_rule")
        var appRule: String? = null,

        /**
         * Kid
         */
        @get:JsonProperty("kid")
        var kid: String? = null,

        /**
         * Terminal Ids
         */
        @get:JsonProperty("terminal_id")
        var terminalId: String? = null,

        /**
         * Icon Encoded String
         */
        @get:JsonProperty("icon_encoded_string")
        var icon: String? = null

)