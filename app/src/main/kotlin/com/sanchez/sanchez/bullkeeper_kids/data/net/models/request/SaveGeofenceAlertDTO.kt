package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Save Geofence Alert DTO
 */
data class SaveGeofenceAlertDTO (

        /**
         * Kid
         */
        @get:JsonProperty("kid")
        var kid: String? = null,

        /**
         * Geofence
         */
        @get:JsonProperty("geofence")
        var geofence: String? = null,

        /**
         * Transition Type
         */
        @get:JsonProperty("type")
        var type: String? = null,

        /**
         * Terminal
         */
        @get:JsonProperty("terminal")
        var terminal: String? = null

)