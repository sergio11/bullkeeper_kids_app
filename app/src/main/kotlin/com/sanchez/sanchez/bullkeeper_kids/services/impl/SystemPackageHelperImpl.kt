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
import timber.log.Timber
import android.content.pm.ApplicationInfo

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
        val apps = pm.getInstalledPackages(0)
        for(app in apps) {

            val applicationInfo = app.applicationInfo
            val isSystemApp = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0

            if((isSystemApp && !getSysPackages) ||
                    (discardAppPackage && app.packageName == context.packageName) ||
                    app.packageName.toLowerCase().contains("launcher") ||
                    app.packageName.toLowerCase().contains("bullkeeper"))
                continue

            val newInfo = SystemPackageInfo()
            newInfo.appName = app.applicationInfo.loadLabel(pm).toString()
            newInfo.packageName = app.packageName
            newInfo.versionName = app.versionName
            newInfo.versionCode = app.versionCode.toString()
            try {
                newInfo.icon = getPackageIconAsEncodedString(app)
            } catch (e: Exception) {
                Timber.e(e.message)
            }
            newInfo.targetSdkVersion = app.applicationInfo.targetSdkVersion
            app.permissions?.let {
                newInfo.permissions = it.joinToString(separator = ",") { it.name }
            }

            res.add(newInfo)


        }
        return res
    }

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