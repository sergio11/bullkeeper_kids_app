package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.ActionBootCompletedBroadcastReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Action Boot Completed Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class])
interface ActionBootCompletedReceiverComponent {

    /**
     * Inject into Action Boot Completed Broadcast Receiver
     */
    fun inject(actionBootCompletedBroadcastReceiver: ActionBootCompletedBroadcastReceiver)
}