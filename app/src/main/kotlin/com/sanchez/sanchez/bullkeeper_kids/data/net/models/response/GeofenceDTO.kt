package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

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
         * Expiration Duration
         */
        @JsonProperty("expiration_duration")
        var expirationDuration: Long? = -1,

        /**
         * Create At
         */
        @JsonProperty("create_at")
        var createAt: Date? = null,

        /**
         * Update At
         */
        @JsonProperty("update_at")
        var updateAt: Date? = null

)