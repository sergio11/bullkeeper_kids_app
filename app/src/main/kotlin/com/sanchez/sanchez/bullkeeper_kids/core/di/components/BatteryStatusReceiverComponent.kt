package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.BatteryStatusBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Battery Status Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class])
interface BatteryStatusReceiverComponent {

    /**
     * Inject into Battery Status Broadcast Receiver
     */
    fun inject(batteryStatusBroadcastReceiver: BatteryStatusBroadcastReceiver)
}