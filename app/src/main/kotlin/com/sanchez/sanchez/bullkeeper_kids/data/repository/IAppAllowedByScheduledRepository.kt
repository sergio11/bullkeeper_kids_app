package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppAllowedByScheduledEntity

/**
 * App Allowed By Scheduled Repository
 */
interface IAppAllowedByScheduledRepository: ISupportRepository<AppAllowedByScheduledEntity> {

    /**
     * Is App Allowed
     */
    fun isAppAllowed(
            app: String, scheduledBlock: String
    ): Boolean

    /**
     * Any App Allowed
     */
    fun anyAppAllowed( scheduledBlock: String): Boolean

    /**
     * Delete By Scheduled Block Id
     */
    fun deleteByScheduledBlockId(id: String)
}