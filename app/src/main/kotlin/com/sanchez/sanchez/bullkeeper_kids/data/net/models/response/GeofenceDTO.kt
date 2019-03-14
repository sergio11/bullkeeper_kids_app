package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Geofence DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofenceDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * Name
         */
        @JsonProperty("name")
        var name: String? = null,

        /**
         * address
         */
        @JsonProperty("address")
        var address: String? = null,

        /**
         * Latitude
         */
        @JsonProperty("lat")
        var lat: Double? = null,

        /**
         * Log
         */
        @JsonProperty("log")
        var log: Double? = null,

        /**
         * Radius
         */
        @JsonProperty("radius")
        var radius: Float? = null,

        /**
         * Type
         */
        @JsonProperty("type")
        var type: String? = null,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Is Enabled
         */
        @JsonProperty("is_enabled")
        var isEnabled: Boolean? = null,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: String? = null,

        /**
         * Update At
         */
        @JsonProperty("update_at")
        var updateAt: String? = null

)