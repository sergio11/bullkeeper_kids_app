package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP_MR1
import com.sanchez.sanchez.bullkeeper_kids.data.entity.SystemPackageUsageStatsEntity
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import java.util.*
import javax.inject.Inject

/**
 * Usage Stats Service Service Impl
 */
class UsageStatsServiceImpl
    @Inject constructor(private val context: Context): IUsageStatsService {

    /**
     * Is Usage Stats Allowed
     */
    override fun isUsageStatsAllowed(): Boolean {

        if (Build.VERSION.SDK_INT < LOLLIPOP_MR1) {
            return false
        }
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),
                context.packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Get Usage Manager
     */
    @TargetApi(LOLLIPOP_MR1)
    private fun getUsageStatsManager(): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    /**
     * The [Comparator] to sort a collection of [UsageStats] sorted by the timestamp
     * last time the app was used in the descendant order.
     */
    private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats> {

        override fun compare(left: UsageStats, right: UsageStats): Int {
            return java.lang.Long.compare(right.lastTimeUsed, left.lastTimeUsed)
        }
    }

    /**
     * Get Daily Stats From A Year
     */
    @TargetApi(LOLLIPOP_MR1)
    override fun getDailyStatsFromAYear(): List<SystemPackageUsageStatsEntity> {
        val usm = getUsageStatsManager()
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis
        val usageStats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startTime, endTime)
        Collections.sort(usageStats, LastTimeLaunchedComparatorDesc())
        val systemPackageUsageStatsList = ArrayList<SystemPackageUsageStatsEntity>()
        for(usageStat in usageStats) {
            val systemPackageUsageStats = SystemPackageUsageStatsEntity()
            systemPackageUsageStats.firstTimeStamp = usageStat.firstTimeStamp
            systemPackageUsageStats.lastTimeStamp = usageStat.lastTimeStamp
            systemPackageUsageStats.lastTimeUsed = usageStat.lastTimeUsed
            systemPackageUsageStats.packageName = usageStat.packageName
            systemPackageUsageStats.totalTimeInForeground = usageStat.totalTimeInForeground
            systemPackageUsageStatsList.add(systemPackageUsageStats)
        }
        return systemPackageUsageStatsList
    }

    /**
     * Get Current Foreground App
     */
    override fun getCurrentForegroundApp(): String? {

        var currentForegroundApp: String? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            val usm = getUsageStatsManager()
            val time = System.currentTimeMillis()
            val appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    time - 1000 * 1000, time)

            if (appList != null && appList.size > 0) {
                val sortedMap = TreeMap<Long, UsageStats>()
                for (usageStats in appList) {
                    sortedMap[usageStats.lastTimeUsed] = usageStats
                }

                if (!sortedMap.isEmpty()) {
                    currentForegroundApp = sortedMap[sortedMap.lastKey()]!!.packageName
                }
            }
        } else {

            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            currentForegroundApp = manager.getRunningTasks(1)[0].topActivity.packageName
        }

        return currentForegroundApp;

    }
}