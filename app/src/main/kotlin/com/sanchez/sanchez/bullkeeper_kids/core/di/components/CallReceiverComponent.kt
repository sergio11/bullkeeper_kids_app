package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ApplicationModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.CallDetailsModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.GlobalServiceModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.NetModule
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.phone.CallReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Call Receiver Component
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class, GlobalServiceModule::class,
            NetModule::class, CallDetailsModule::class])
interface CallReceiverComponent {

    /**
     * Inject into Call Receiver Broadcast
     */
    fun inject(callReceiver: CallReceiver)
}