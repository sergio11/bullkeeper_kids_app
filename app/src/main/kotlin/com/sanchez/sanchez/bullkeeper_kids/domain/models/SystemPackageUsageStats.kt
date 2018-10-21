package com.sanchez.sanchez.bullkeeper_kids.domain.models

import android.util.Log
import java.io.Serializable

/**
 * System Package Usage Stats
 */
data class SystemPackageUsageStats(
        // First Time Stamp
        var firstTimeStamp: Long = 0,
        // Last Time Used
        var lastTimeUsed: Long = 0,
        // Last Time Stamp
        var lastTimeStamp: Long = 0,
        // Total Time In Foreground
        var totalTimeInForeground: Long = 0,
        // Package Name
        var packageName: String = ""
) : Serializable {

    val TAG = "SYSTEM_PACKAGE_USAGE"

    /**
     * Pretty Print
     */
    fun prettyPrint() {
        Log.d(TAG, "Package Name: $packageName")
        Log.d(TAG, "First Time Stamp: $firstTimeStamp")
        Log.d(TAG, "Last Time Used: $lastTimeUsed")
        Log.d(TAG, "Last Time Stamp: $lastTimeStamp")
        Log.d(TAG, "Total Time In Foreground: $totalTimeInForeground")
        Log.d(TAG, "Package Name: $packageName")
    }

    /**
     *
     */
    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SystemPackageInfo
        return this.packageName == other.packageName
    }
}