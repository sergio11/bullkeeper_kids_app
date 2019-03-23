package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.ActionBootCompletedBroadcastReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AwakenMonitoringServiceBroadcastReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.ShutdownBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Awaken Monitoring Service Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class])
interface AwakenMonitoringServiceReceiverComponent {

    /**
     * Inject into Awaken Monitoring Service Broadcast Receiver
     */
    fun inject(awakenMonitoringServiceBroadcastReceiver: AwakenMonitoringServiceBroadcastReceiver)
}