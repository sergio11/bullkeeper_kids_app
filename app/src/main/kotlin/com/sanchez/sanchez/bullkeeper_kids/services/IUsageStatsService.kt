package com.sanchez.sanchez.bullkeeper_kids.services


import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageUsageStats


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
    fun getDailyStatsFromAYear(): List<SystemPackageUsageStats>

    /**
     * Get Current Foreground App
     */
    fun getCurrentForegroundApp(): String?


}