package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Person DTO
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonDTO(

        /**
         * Identity
         */
        @JsonProperty("identity")
        var identity: String? = null,

        /**
         * First Name
         */
        @JsonProperty("first_name")
        var firstName: String? = null,

        /**
         * Last Name
         */
        @JsonProperty("last_name")
        var lastName: String? = null,

        /**
         * Profile Image
         */
        @JsonProperty("profile_image")
        var profileImage: String? = null

)