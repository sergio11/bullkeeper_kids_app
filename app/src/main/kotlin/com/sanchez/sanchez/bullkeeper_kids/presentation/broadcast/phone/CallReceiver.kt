package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.phone

import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.CallReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.AddCallDetailsFromTerminalInteract
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Call Receiver
 */
class CallReceiver: PhoneCallReceiver() {

    /**
     * Call Receiver Component
     */
    private val callReceiverComponent: CallReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.callReceiverComponent
    }

    /**
     * Add Call Details From Terminal Internal
     */
    @Inject
    internal lateinit var addCallDetailsFromTerminalInteract: AddCallDetailsFromTerminalInteract

    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        callReceiverComponent.inject(this)
    }

    /**
     * On Incoming Call Started
     */
    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onIncomingCallStarted(ctx, number, start)
        Timber.d("onIncomingCallStarted -> %s", number)
    }

    /**
     * On Outgoing Call Started
     */
    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onOutgoingCallStarted(ctx, number, start)
        Timber.d("onOutgoingCallStarted -> %s", number)
    }

    /**
     * On Incoming Call Ended
     */
    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onIncomingCallEnded(ctx, number, start, end)
        Timber.d("onIncomingCallEnded -> %s", number)
    }

    /**
     * On Outgoing Call Ended
     */
    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onOutgoingCallEnded(ctx, number, start, end)
        Timber.d("onOutgoingCallEnded -> %s", number)
    }

    /**
     * On Missed Call
     */
    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
        super.onMissedCall(ctx, number, start)
        Timber.d("onMissedCall -> %s", number)
    }
}