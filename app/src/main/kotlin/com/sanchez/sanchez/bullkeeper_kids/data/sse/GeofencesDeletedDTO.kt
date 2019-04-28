package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Geofences Deleted DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeofencesDeletedDTO(

        /**
         * Ids
         */
        @JsonProperty("ids")
        var ids: List<String>,

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String
)