package com.sanchez.sanchez.bullkeeper_kids.services


import com.sanchez.sanchez.bullkeeper_kids.data.entity.SystemPackageUsageStatsEntity


/**
 * Usage Stats
 */
interface IUsageStatsService {


    /**
     * Is Usage Stats Allowed
     */
    fun isUsageStatsAllowed(): Boolean

    /**
     * Get Daily Stats From Year
     */
    fun getDailyStatsFromAYear(): List<SystemPackageUsageStatsEntity>

    /**
     * Get Current Foreground App
     */
    fun getCurrentForegroundApp(): String?


}