package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.pm.PackageManager
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper


/**
 * System Package Helper Impl
 */
class SystemPackageHelperImpl: ISystemPackageHelper {

    /**
     * Get Installed Apps
     */
    override fun getInstalledApps(pm: PackageManager, getSysPackages: Boolean): ArrayList<SystemPackageInfo> {
        val res = ArrayList<SystemPackageInfo>()
        val packs = pm.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs.get(i)
            if (!getSysPackages && p.versionName == null) {
                continue
            }
            val newInfo = SystemPackageInfo()
            newInfo.appName = p.applicationInfo.loadLabel(pm).toString()
            newInfo.packageName = p.packageName
            newInfo.versionName = p.versionName
            newInfo.versionCode = p.versionCode.toString()
            newInfo.icon = p.applicationInfo.loadIcon(pm)
            res.add(newInfo)
        }
        return res
    }

    /**
     * Get Packages
     */
    override fun getPackages(pm: PackageManager): ArrayList<SystemPackageInfo> {
        val apps = getInstalledApps(pm,false) /* false = no system packages */
        val max = apps.size
        for (i in 0 until max) {
            apps[i].prettyPrint()
        }
        return apps
    }
}