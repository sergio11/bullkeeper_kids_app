package com.sanchez.sanchez.bullkeeper_kids.domain.models

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.GuardianRolesEnum


/**
 * Supervised Children Entity
 */
data class SupervisedChildrenEntity (

        /**
         * Identity
         */
        var identity: String? = null,

        /**
         * Role
         */
        var role: GuardianRolesEnum? = null,

        /**
         * Kid Entity
         */
        var kid: KidEntity? = null
)