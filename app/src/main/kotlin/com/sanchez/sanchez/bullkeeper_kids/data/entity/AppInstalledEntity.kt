package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * App Installed Entity
 */
open class AppInstalledEntity(
        // Package Name
        @PrimaryKey var packageName: String? = "",
        // First Install Time
        var firstInstallTime: Long?  = 0,
        // Last Update Time
        var lastUpdateTime: Long?  = 0,
        // Version Name
        var versionName: String? = "",
        // Version Code
        var versionCode: String? = "",
        // App Name
        var appName: String? = "",
        // App Rules
        var appRule: String? = AppRuleEnum.PER_SCHEDULER.name,
        // App Icon
        var icon: String? = null,
        // App Category
        var category: String? = null,
        // Disabled
        var disabled: Boolean = false,
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = null,
        // Removed
        var removed: Boolean = false
) : RealmObject(), Serializable