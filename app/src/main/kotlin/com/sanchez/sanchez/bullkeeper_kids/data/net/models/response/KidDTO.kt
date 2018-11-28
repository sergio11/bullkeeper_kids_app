package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Kid DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class KidDTO(

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
         * Birth Date
         */
        @JsonProperty("birthdate")
        var birthdate: Date? = null,

        /**
         * Age
         */
        @JsonProperty("age")
        var age: Int? = null,

        /**
         * School
         */
        @JsonProperty("school")
        var schoolDTO: SchoolDTO? = null,

        /**
         * Profile Image
         */
        @JsonProperty("profile_image")
        var profileImage: String? = null,

        /**
         * Terminals
         */
        @JsonProperty("terminals")
        var terminals: List<TerminalDTO>? = null

)