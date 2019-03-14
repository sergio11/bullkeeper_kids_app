package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Geofence Saved DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofenceSavedDTO(


        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String,

        /**
         * Name
         */
        @JsonProperty("name")
        var name: String,

        /**
         * address
         */
        @JsonProperty("address")
        var address: String,

        /**
         * Latitude
         */
        @JsonProperty("lat")
        var lat: Double,

        /**
         * Longitude
         */
        @JsonProperty("log")
        var log: Double,

        /**
         * Radius
         */
        @JsonProperty("radius")
        var radius: Float,

        /**
         * Type
         */
        @JsonProperty("type")
        var type: String,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Is Enabled
         */
        @JsonProperty("is_enabled")
        var isEnabled: Boolean,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: String,

        /**
         * Update At
         */
        @JsonProperty("update_at")
        var updateAt: String
)