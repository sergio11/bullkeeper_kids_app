package com.sanchez.sanchez.bullkeeper_kids.services

import android.content.pm.PackageManager
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo

/**
 * System Package Helper
 */
interface ISystemPackageHelper {

    /**
     * Get Packages
     */
    fun getPackages(pm: PackageManager): ArrayList<SystemPackageInfo>


    /**
     * Get Installed Apps
     */
    fun getInstalledApps(pm: PackageManager, getSysPackages: Boolean): ArrayList<SystemPackageInfo>


}