package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

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
         * Kid
         */
        @JsonProperty("kid")
        var kid: String
)