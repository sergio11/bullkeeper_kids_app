package com.sanchez.sanchez.bullkeeper_kids.services

import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo

/**
 * System Package Helper
 */
interface ISystemPackageHelper {

    /**
     * Get Packages
     */
    fun getPackages(): ArrayList<SystemPackageInfo>

    /**
     * Get Pacakage Info
     */
    fun getPackageInfo(packageName: String): SystemPackageInfo?


    /**
     * Get Installed Apps
     */
    fun getInstalledApps(getSysPackages: Boolean, discardAppPackage: Boolean = true): ArrayList<SystemPackageInfo>


}