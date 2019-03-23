package com.sanchez.sanchez.bullkeeper_kids.domain.models

data class TerminalEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * App Version Name
         */
        var appVersionName: String? = null,

        /**
         * App Version Code
         */
        var appVersionCode: String? = null,

        /**
         * Os Version
         */
        var osVersion: String? = null,

        /**
         * SDK Version
         */
        var sdkVersion: String? = null,

        /**
         * Manufacturer
         */
        var manufacturer: String? = null,

        /**
         * Market Name
         */
        var marketName: String? = null,

        /**
         * Model
         */
        var model: String? = null,

        /**
         * Code Name
         */
        var codeName: String? = null,

        /**
         * Device Name
         */
        var deviceName: String? = null,

        /**
         * Device Id
         */
        var deviceId: String? = null,


        /**
         * Kid Id
         */
        var kidId: String? = null,

        /**
         * Bed Time Enabled
         */
        var bedTimeEnabled: Boolean = true,

        /**
         * Screen Enabled
         */
        var screenEnabled: Boolean = true,

        /**
         * Camera Enabled
         */
        var cameraEnabled: Boolean = true,

        /**
         * Settings Enabled
         */
        var settingsEnabled: Boolean = false,

        /**
         * Phone Calls Enabled
         */
        var phoneCallsEnabled: Boolean = true

)