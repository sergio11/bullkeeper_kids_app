package com.sanchez.sanchez.bullkeeper_kids.domain.models

import android.graphics.drawable.Drawable
import android.util.Log
import java.io.Serializable

/**
 * System Package Info
 */
data class SystemPackageInfo (
        // Package Name
        var packageName: String = "",
        // First Install Time
        var firstInstallTime: Long = 0,
        // Last Update Time
        var lastUpdateTime: Long = 0,
        // Version Name
        var versionName: String = "",
        // Version Code
        var versionCode: String = "",
        // App Name
        var appName: String = "",
        // App Icon
        var icon: Drawable? = null
) : Serializable {

    val TAG = "SYSTEM_PACKAGE_INFO"

    /**
     * Pretty Print
     */
    fun prettyPrint() {
        Log.d(TAG, "Package Name: $packageName")
        Log.d(TAG, "First Installing time: $firstInstallTime")
        Log.d(TAG, "Last Update Time: $lastUpdateTime")
        Log.d(TAG, "Version: $versionName - $versionCode")
        Log.d(TAG, "Application Name: $appName")
    }

}