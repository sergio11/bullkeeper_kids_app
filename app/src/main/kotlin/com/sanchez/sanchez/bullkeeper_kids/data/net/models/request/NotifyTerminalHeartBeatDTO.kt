package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Notify Terminal HeartBeat DTO
 */
data class NotifyTerminalHeartBeatDTO (

        /**
         * Kid
         */
        @get:JsonProperty("id")
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
        var adminAccessEnabled: Boolean = false


)