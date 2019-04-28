package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Geofence Alert DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofenceAlertDTO(

        /**
         * Date
         */
        @JsonProperty("date")
        var date: String,

        /**
         * Type
         */
        @JsonProperty("type")
        var type: String,

        /**
         * Title
         */
        @JsonProperty("title")
        var title: String,

        /**
         * Message
         */
        @JsonProperty("description")
        var message: String

)