package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * School DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SchoolDTO(

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
         * Residence
         */
        @JsonProperty("residence")
        var residence: String? = null,

        /**
         * Latitude
         */
        @JsonProperty("latitude")
        var latitude: Double? = null,

        /**
         * Longitude
         */
        @JsonProperty("longitude")
        var longitude: Double? = null,

        /**
         * Province
         */
        @JsonProperty("province")
        var province: String? = null,

        /**
         * Tfno
         */
        @JsonProperty("tfno")
        var tfno: String? = null,


        /**
         * Email
         */
        @JsonProperty("email")
        var email: String? = null

) {}