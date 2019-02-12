package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * All Geofence Deleted DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AllGeofenceDeletedDTO(

        /**
         * Kid
         */
        @JsonProperty("id")
        var kid: String
)