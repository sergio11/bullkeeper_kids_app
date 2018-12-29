package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SMSReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import timber.log.Timber
import javax.inject.Inject

/**
 * SMS Broadcast Receiver
 */
class SMSBroadcastReceiver : BroadcastReceiver()  {

    /**
     * Sms Receiver Component
     */
    private val smsReceiverComponent: SMSReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.smsReceiverComponent
    }

    /**
     * Synchronize Terminal SMS Interact
     */
    @Inject
    internal lateinit var synchronizeTerminalSMSInteract: SynchronizeTerminalSMSInteract

    /**
     * On Receive
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        smsReceiverComponent.inject(this)
        synchronizeTerminalSMSInteract(UseCase.None()){
            it.either(fnL = fun(_:Failure) {
                Timber.d("Sync SMS Failed")
            }, fnR = fun(total: Int) {
                Timber.d("Sync Terminal SMS, total -> %d", total)
            })
        }
    }
}