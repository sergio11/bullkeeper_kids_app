package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import android.content.pm.PackageInfo
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.ByteArrayOutputStream
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.graphics.drawable.Drawable
import android.content.pm.ApplicationInfo
import com.sanchez.sanchez.bullkeeper_kids.core.extension.empty

/**
 * System Package Helper Impl
 */
class SystemPackageHelperImpl
    @Inject constructor(private val context: Context): ISystemPackageHelper {

    private val pm = context.packageManager

    /**
     * Get Installed Apps
     */
    override fun getInstalledApps(getSysPackages: Boolean, discardAppPackage: Boolean): List<SystemPackageInfo>
     = pm.getInstalledPackages(0).filter {
        val applicationInfo = it.applicationInfo
        val isSystemApp = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        pm.getLaunchIntentForPackage(it.packageName) != null &&
                !((isSystemApp && !getSysPackages) ||
                        (discardAppPackage && it.packageName == context.packageName) ||
                        it.packageName.toLowerCase().contains("launcher") ||
                        it.packageName.toLowerCase().contains("bullkeeper") ||
                        it.packageName.toLowerCase() =="com.android.settings")

    }.map {
        SystemPackageInfo().apply {
            appName = it.applicationInfo.loadLabel(pm).toString()
            packageName = it.packageName
            firstInstallTime  = it.firstInstallTime
            lastUpdateTime = it.lastUpdateTime
            versionName = it.versionName
            versionCode = it.versionCode.toString()
            icon = getPackageIconAsEncodedString(it)
            targetSdkVersion = it.applicationInfo.targetSdkVersion
            permissions = it.permissions?.joinToString(separator = ",") { it.name } ?: String.empty()
        }
    }.toList()


    /**
     * Drawable To Bitmap
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    /**
     * Get Package Icon As Encoded String
     */
    private fun getPackageIconAsEncodedString(packageInfo :PackageInfo?): String?{
        packageInfo?.applicationInfo?.loadIcon(pm)?.let {
            val bitmap = drawableToBitmap(it)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bitMapData = stream.toByteArray()
            return Base64.encodeToString(bitMapData, Base64.DEFAULT)
        }
    }

    /**
     * Get Packages
     */
    override fun getPackages(): List<SystemPackageInfo> {
        val apps = getInstalledApps(true) /* false = no system packages */
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