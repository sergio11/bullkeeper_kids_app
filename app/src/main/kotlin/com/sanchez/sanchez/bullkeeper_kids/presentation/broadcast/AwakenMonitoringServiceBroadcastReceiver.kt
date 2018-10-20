package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.ActivityManager
import android.support.v4.content.ContextCompat
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService


/**
 * Awaken Monitoring Service Broadcast Receiver
 */
class AwakenMonitoringServiceBroadcastReceiver : BroadcastReceiver() {

    /**
     * Check Monitoring Service Status
     */
    private fun isMonitoringServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MonitoringService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {
        if(!isMonitoringServiceRunning(context))
            ContextCompat.startForegroundService(context, Intent(context, MonitoringService::class.java))
    }
}
