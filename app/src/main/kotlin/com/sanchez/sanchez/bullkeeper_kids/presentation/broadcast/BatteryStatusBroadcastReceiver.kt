package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.BatteryStatusReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber
import javax.inject.Inject
import android.os.BatteryManager



/**
 * Battery Status Broadcast Receiver
 */
class BatteryStatusBroadcastReceiver : BroadcastReceiver()  {

    /**
     * Battery Status Receiver Component
     */
    private val batteryStatusComponent: BatteryStatusReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.batteryStatusReceiverComponent
    }

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * On Receive
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        batteryStatusComponent.inject(this)

        Timber.d("BKA_63: Battery Level Status change")

        intent?.let {
            val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            preferenceRepository.setBatteryChargingEnabled(status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL)

            val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            preferenceRepository.setBatteryLevel(level)

        }

    }
}