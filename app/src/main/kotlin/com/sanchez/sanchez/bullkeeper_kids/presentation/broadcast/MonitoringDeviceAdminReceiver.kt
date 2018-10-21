package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Monitoring Device Admin Receiver
 */
class MonitoringDeviceAdminReceiver : DeviceAdminReceiver() {

    val TAG = "MON_DEVICE_ADMIN"

    companion object {
        @JvmStatic
        fun getComponentName(context: Context) =
                ComponentName(context, MonitoringDeviceAdminReceiver::class.java)
    }

    override fun onEnabled(context: Context?, intent: Intent?) {
        Log.d(TAG, "Enable Device Admin")
    }

    override fun onDisabled(context: Context?, intent: Intent?) {
        Log.d(TAG, "Disable Device Admin")
    }
}
