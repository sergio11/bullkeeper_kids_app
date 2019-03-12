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
        var terminal: String? = null,

        /**
         * Access Fine Location
         */
        @get:JsonProperty("access_fine_location_enabled")
        var accessFineLocationEnabled: Boolean = false,

        /**
         * Read Contacts
         */
        @get:JsonProperty("read_contacts_enabled")
        var readContactsEnabled: Boolean = false,

        /**
         * Read Call Log Enabled
         */
        @get:JsonProperty("read_call_log_enabled")
        var readCallLogEnabled: Boolean = false,

        /**
         * Write External Storage Enabled
         */
        @get:JsonProperty("write_external_storage_enabled")
        var writeExternalStorageEnabled: Boolean = false,

        /**
         * Usage Stats Allowed
         */
        @get:JsonProperty("usage_stats_allowed")
        var usageStatsAllowed: Boolean = false,

        /**
         * Admin Access Enabled
         */
        @get:JsonProperty("admin_access_enabled")
        var adminAccessEnabled: Boolean = false,

        /**
         * Battery Level
         */
        @get:JsonProperty("battery_level")
        var batteryLevel: Int = 0,

        /**
         * Is Battery Charging
         */
        @get:JsonProperty("is_battery_charging")
        var isBatteryCharging: Boolean = false,

        /**
         * High Accuranccy Location Enabled
         */
        @get:JsonProperty("high_accuraccy_location_enabled")
        var highAccuraccyLocation: Boolean = false,


        /**
         * Apps Overlay Enabled
         */
        @get:JsonProperty("apps_overlay_enabled")
        var appsOverlayEnabled: Boolean = false

)