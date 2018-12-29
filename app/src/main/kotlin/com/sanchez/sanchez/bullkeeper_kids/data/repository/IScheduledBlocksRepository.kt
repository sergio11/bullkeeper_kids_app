package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import org.joda.time.LocalTime

/**
 * Scheduled Block Repository
 */
interface IScheduledBlocksRepository: ISupportRepository<ScheduledBlockEntity> {

    /**
     * Delete By Kid
     */
    fun deleteByKid(kid: String)


    /**
     * Delete By Kid And Block
     */
    fun deleteByKidAndBlock(kid: String, block: String)

    /**
     * Update Status
     */
    fun updateStatus(id: String, status: Boolean)


    /**
     * Update Image
     */
    fun updateImage(id: String, image: String)

    /**
     * Any Scheduled Block Enable
     */
    fun anyScheduledBlockEnable(now: LocalTime, localTimeFormat: String): Boolean

    /**
     * Any Scheduled Block Enable for this moment
     */
    fun anyScheduledBlockEnableForThisMoment(localTimeFormat: String): Boolean

    /**
     * Get Scheduled Block Enable For
     */
    fun getScheduledBlockEnableFor(moment: LocalTime, localTimeFormat: String): ScheduledBlockEntity?

    /**
     * Get Scheduled Block Enable For this moment
     */
    fun getScheduledBlockEnableForThisMoment(localTimeFormat: String): ScheduledBlockEntity?


}