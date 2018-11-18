package com.sanchez.sanchez.bullkeeper_kids.domain.models

/**
 * School Entity
 */
data class SchoolEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * Name
         */
        var name: String? = null,

        /**
         * Residence
         */
        var residence: String? = null,

        /**
         * Latitude
         */
        var latitude: Double? = null,

        /**
         * Longitude
         */
        var longitude: Double? = null,

        /**
         * Province
         */
        var province: String? = null,

        /**
         * Tfno
         */
        var tfno: String? = null,

        /**
         * Email
         */
        var email: String? = null
)