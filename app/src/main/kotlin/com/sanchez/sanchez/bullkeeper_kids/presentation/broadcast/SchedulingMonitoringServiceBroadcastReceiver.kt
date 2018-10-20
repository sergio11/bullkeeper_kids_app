package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.app.AlarmManager
import android.app.PendingIntent

/**
 * Scheduling Monitoring Service
 */
class SchedulingMonitoringServiceBroadcastReceiver : BroadcastReceiver() {

    private val PERIOD: Long = 60000

    /**
     * Schedule Alarms
     */
    private fun scheduleAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AwakenMonitoringServiceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pendingIntent)
    }

    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) =
        scheduleAlarms(context)


}
