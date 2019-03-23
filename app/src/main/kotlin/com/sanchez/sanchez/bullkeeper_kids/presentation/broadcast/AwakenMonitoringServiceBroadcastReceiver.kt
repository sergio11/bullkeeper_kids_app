package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.support.v4.content.ContextCompat
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AwakenMonitoringServiceReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import timber.log.Timber
import javax.inject.Inject


/**
 * Awaken Monitoring Service Broadcast Receiver
 */
class AwakenMonitoringServiceBroadcastReceiver : BroadcastReceiver() {

    /**
     * Awaken Monitoring Service Component
     */
    private val awakenMonitoringServiceComponent: AwakenMonitoringServiceReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.awakenMonitoringServiceComponent
    }

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository


    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {
        awakenMonitoringServiceComponent.inject(this)
        Timber.d("BKA_59: Awaken Monitoring Service launched")

        if(preferenceRepository.getPrefKidIdentity()
                != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefTerminalIdentity() !=
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {

            if (!isMonitoringServiceRunning(context)) {
                Timber.d("BKA_59: Monitoring Service is not running")
                ContextCompat.startForegroundService(context, Intent(context, MonitoringService::class.java))
            }

            AwakenMonitoringServiceBroadcastReceiver.scheduledAt(context)

        }

    }


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


    companion object {

        /**
         * Scheduled
         */
        fun scheduledAt(context: Context){
            Timber.d("BKA_59: Schedule Alarm for Awaken Monitoring Service")
            val i = Intent(context, AwakenMonitoringServiceBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0)
            val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
        }


    }


}
