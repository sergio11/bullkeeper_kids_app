package com.sanchez.sanchez.bullkeeper_kids.domain.models

import java.io.Serializable
import java.util.*

/**
 * Kid Entity
 */
data class KidEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * First Name
         */
        var firstName: String? = null,

        /**
         * Last Name
         */
        var lastName: String? = null,

        /**
         * Birth Date
         */
        var birthdate: Date? = null,

        /**
         * Age
         */
        var age: Int? = null,

        /**
         * School
         */
        var school: SchoolEntity? = null,

        /**
         * Profile Image
         */
        var profileImage: String? = null,

        /**
         * Terminals
         */
        var terminals: List<TerminalEntity>? = null
): Serializable