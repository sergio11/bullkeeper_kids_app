package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Installed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppInstalledDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,


        /**
         * Package Name
         */
        @JsonProperty("package_name")
        var  packageName: String? = null,

        /**
         * First Install Time
         */
        @JsonProperty("first_install_time")
        var firstInstallTime: Long? = null,


        /**
         * Last Update Time
         */
        @JsonProperty("last_update_time")
        var lastUpdateTime: Long? = null,

        /**
         * Version Name
         */
        @JsonProperty("version_name")
        var versionName: String? = null,

        /**
         * Version Code
         */
        @JsonProperty("version_code")
        var versionCode: String? = null,

        /**
         * App Name
         */
        @JsonProperty("app_name")
        var appName: String? = null,

        /**
         * App Rule
         */
        @JsonProperty("app_rule")
        var appRule: String? = null,

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: String? = null,

        /**
         * Terminal ID
         */
        @JsonProperty("terminal_id")
        var terminal: String? = null,

        /**
         * Icon Encoded String
         */
        @JsonProperty("icon_encoded_string")
        var icon: String? = null,

        /**
         * Category
         */
        @JsonProperty("category")
        var category: String? = null


)