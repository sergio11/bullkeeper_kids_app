package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Geofence Status Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofenceStatusChangedDTO(

        /**
         * Geofence
         */
        @JsonProperty("geofence")
        var geofence: String,


        /**
         * kid
         */
        @JsonProperty("kid")
        var kid: String,

        /**
         * Is Enabled
         */
        @JsonProperty("is_enabled")
        var isEnabled: Boolean
)