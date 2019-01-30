package com.sanchez.sanchez.bullkeeper_kids.domain.models

/**
 * Person Entity
 */
data class PersonEntity(

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
         * Profile Image
         */
        var profileImage: String? = null

)