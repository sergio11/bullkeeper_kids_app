package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
    Device Photo DTO
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
data class DevicePhotoDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        val identity: String,

        /**
         * Local Id
         */
        @JsonProperty("local_id")
        val localId: String,

        /**
         * Path
         */
        @JsonProperty("path")
        val path: String

)


