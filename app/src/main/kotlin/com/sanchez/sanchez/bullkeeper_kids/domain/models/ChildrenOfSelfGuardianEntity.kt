package com.sanchez.sanchez.bullkeeper_kids.domain.models

/**
 * Children Of Self Guardian Entity
 */
data class ChildrenOfSelfGuardianEntity (

        /**
         * Total
         */
        var total: Long? = null,

        /**
         * Confirmed
         */
        var confirmed: Long? = null,

        /**
         * No Confirmed
         */
        var noConfirmed: Long? = null,

        /**
         * Supervised Children
         */
        var supervisedChildrenList: List<SupervisedChildrenEntity>? = null
)