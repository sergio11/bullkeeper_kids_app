package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Device Photo Disable DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DevicePhotoDisabledDTO(

        /**
         * Identity
         */
        @JsonProperty("kid")
        val kid: String,

        /**
         * Terminal
         */
        @JsonProperty("terminal")
        val terminal: String,

        /**
         * Photo
         */
        @JsonProperty("photo")
        val photo: String,

        /**
         * Path
         */
        @JsonProperty("path")
        val path: String,

        /**
         * Local Id
         */
        @JsonProperty("local_id")
        val localId: String
)