package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * App Installed Entity
 */
open class AppInstalledEntity(
        // Identity
        @PrimaryKey var identity: String? = "",
        // Package Name
        var packageName: String? = "",
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
        // App Rule
        var appRule: String? = AppRuleEnum.PER_SCHEDULER.name
) : RealmObject(), Serializable