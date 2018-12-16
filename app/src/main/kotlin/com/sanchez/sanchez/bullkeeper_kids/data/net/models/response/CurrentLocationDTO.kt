package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Current Location DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentLocationDTO(

        /**
         * Address
         */
        @JsonProperty("address")
        var address: String? = null,

        /**
         * Latitude
         */
        @JsonProperty("latitude")
        var latitude: Double? = null,

        /**
         * Longitude
         */
        @JsonProperty("longitude")
        var longitude: Double? = null
)