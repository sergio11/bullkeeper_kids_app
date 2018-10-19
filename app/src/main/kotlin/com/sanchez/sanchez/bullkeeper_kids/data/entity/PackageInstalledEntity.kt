package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Package Installed Entity
 */
open class PackageInstalledEntity(
        // Package Name
         @PrimaryKey var packageName: String = "",
        // First Install Time
        var firstInstallTime: Long = 0,
        // Last Update Time
        var lastUpdateTime: Long = 0,
        // Version Name
        var versionName: String = "",
        // Version Code
        var versionCode: String = "",
        // App Name
        var appName: String = ""
) : RealmObject()