package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Geofence Deleted DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofenceDeletedDTO(

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
         * Log
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
         * Expiration Duration
         */
        @JsonProperty("expiration_duration")
        var expirationDuration: Long = -1,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: Date,

        /**
         * Update At
         */
        @JsonProperty("update_at")
        var updateAt: Date
)