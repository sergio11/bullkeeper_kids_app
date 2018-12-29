package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.SMSBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * SMS Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class,
            NetModule::class, SmsModule::class])
interface SMSReceiverComponent {

    /**
     * Inject into Sms Broadcast Receiver
     */
    fun inject(smsBroadcastReceiver: SMSBroadcastReceiver)
}