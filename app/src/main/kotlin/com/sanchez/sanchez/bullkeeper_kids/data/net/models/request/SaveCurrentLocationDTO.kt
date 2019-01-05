package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Save Current Location dto
 */
data class SaveCurrentLocationDTO (

        /**
         * Latitude
         */
        @get:JsonProperty("latitude")
        var latitude: Double? = null,

        /**
         * Longitude
         */
        @get:JsonProperty("longitude")
        var longitude: Double? = null
)