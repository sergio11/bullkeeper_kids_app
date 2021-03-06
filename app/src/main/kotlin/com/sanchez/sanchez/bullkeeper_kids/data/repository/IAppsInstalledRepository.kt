package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity

/**
 * Apps Installed Repository
 */
interface IAppsInstalledRepository: ISupportRepository<AppInstalledEntity> {

    /**
     * Find By Package Name
     */
    fun findByPackageName(packageName: String): AppInstalledEntity?

    /**
     * Find By Package Name In
     */
    fun findByPackageNameIn(values: Array<String>): List<AppInstalledEntity>

    /**
     * Update App Rule
     */
    fun updateAppRule(app: String, appRule: String)

    /**
     * Update App Disabled Status
     */
    fun updateAppDisabledStatus(app: String, disabled: Boolean)

}