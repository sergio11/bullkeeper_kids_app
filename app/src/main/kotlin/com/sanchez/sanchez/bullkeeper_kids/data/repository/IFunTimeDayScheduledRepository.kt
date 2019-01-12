package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.FunTimeDayScheduledEntity

/**
 * Fun Time Day Scheduled Repository
 */
interface IFunTimeDayScheduledRepository: ISupportRepository<FunTimeDayScheduledEntity> {

    /**
     * Any Scheduled Block Enable for this moment
     */
    fun getFunTimeDayScheduledForDay(dayName: String): FunTimeDayScheduledEntity?
}