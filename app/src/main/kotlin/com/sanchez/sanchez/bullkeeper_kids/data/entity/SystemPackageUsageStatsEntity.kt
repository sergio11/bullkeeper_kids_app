package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * System Package Usage Stats
 */
open class SystemPackageUsageStatsEntity(
        // Package Name
        @PrimaryKey var packageName: String = "",
        // First Time Stamp
        var firstTimeStamp: Long = 0,
        // Last Time Used
        var lastTimeUsed: Long = 0,
        // Last Time Stamp
        var lastTimeStamp: Long = 0,
        // Total Time In Foreground
        var totalTimeInForeground: Long = 0,
        // Server id
        var serverId: String? = null,
        // Sync
        var sync: Int = 0,
        // Remove
        var remove: Int = 0
) : RealmObject(),  Serializable