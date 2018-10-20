package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import javax.inject.Inject


/**
 * System Package Helper Impl
 */
class SystemPackageHelperImpl
    @Inject constructor(private val context: Context): ISystemPackageHelper {

    private val pm = context.packageManager

    /**
     * Get Installed Apps
     */
    override fun getInstalledApps(getSysPackages: Boolean, discardAppPackage: Boolean): ArrayList<SystemPackageInfo> {
        val res = ArrayList<SystemPackageInfo>()
        val packs = pm.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs.get(i)
            if ((!getSysPackages && p.versionName == null) ||
                    (discardAppPackage && p.packageName == context.packageName))
                continue
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
    override fun getPackages(): ArrayList<SystemPackageInfo> {
        val apps = getInstalledApps(false) /* false = no system packages */
        val max = apps.size
        for (i in 0 until max) {
            apps[i].prettyPrint()
        }
        return apps
    }

    /**
     * Get Package Info
     */
    override fun getPackageInfo(packageName: String):
            SystemPackageInfo? {

        val packageList = getPackages()
        return packageList.find {
            it.packageName == packageName
        }
    }

}