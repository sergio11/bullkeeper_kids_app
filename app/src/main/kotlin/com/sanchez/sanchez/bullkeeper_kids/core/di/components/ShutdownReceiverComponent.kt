package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.ActionBootCompletedBroadcastReceiver
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.ShutdownBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Shutdown Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class, NetModule::class, TerminalModule::class])
interface ShutdownReceiverComponent {

    /**
     * Inject into Shutdown Broadcast Receiver
     */
    fun inject(shutdownBroadcastReceiver: ShutdownBroadcastReceiver)
}