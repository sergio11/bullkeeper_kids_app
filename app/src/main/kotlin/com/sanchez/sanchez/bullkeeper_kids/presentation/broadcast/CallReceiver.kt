package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.Context
import timber.log.Timber
import java.util.*

class CallReceiver: PhoneCallReceiver() {


    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onIncomingCallStarted(ctx, number, start)
        Timber.d("onIncomingCallStarted -> %s", number)
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onOutgoingCallStarted(ctx, number, start)
        Timber.d("onOutgoingCallStarted -> %s", number)
    }

    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onIncomingCallEnded(ctx, number, start, end)
        Timber.d("onIncomingCallEnded -> %s", number)
    }

    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onOutgoingCallEnded(ctx, number, start, end)
        Timber.d("onOutgoingCallEnded -> %s", number)
    }

    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
        super.onMissedCall(ctx, number, start)
        Timber.d("onOutgoingCallEnded -> %s", number)
    }
}