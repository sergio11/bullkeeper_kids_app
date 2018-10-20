package com.sanchez.sanchez.bullkeeper_kids.services

import android.app.usage.UsageStats


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
    fun getDailyStatsFromAYear(): List<UsageStats>

    /**
     * Get Current Foreground App
     */
    fun getCurrentForegroundApp(): String?


}