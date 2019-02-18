package com.sanchez.sanchez.bullkeeper_kids.domain.models

import java.io.Serializable
import java.util.*

/**
 * Kid Guardian
 */
data class KidGuardianEntity(

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * Kid
         */
        var kid: KidEntity? = null,


        /**
         * Is Confirmed
         */
        var isConfirmed: Boolean = false,

        /**
         * Request At
         */
        var requestAt: Date? = null,


        /**
         * Guardian
         */
        var guardian: GuardianEntity? = null,

        /**
         * Role
         */
        var role: GuardianRolesEnum? = null

): Serializable