package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Location DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationDTO(


        /**
         * Latitude
         */
        @JsonProperty("latitude")
        var lat: Double? = null,

        /**
         * Longitude
         */
        @JsonProperty("longitude")
        var log: Double? = null,

        /**
         * Address
         */
        @JsonProperty("address")
        var address: String? = null

)