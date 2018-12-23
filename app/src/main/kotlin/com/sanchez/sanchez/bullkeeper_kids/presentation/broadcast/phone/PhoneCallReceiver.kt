package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.phone

import android.content.BroadcastReceiver
import android.content.Context
import android.telephony.TelephonyManager
import android.content.Intent
import com.android.internal.telephony.ITelephony
import java.util.*
import timber.log.Timber


/**
 * Phone Call Receiver
 */
abstract class PhoneCallReceiver : BroadcastReceiver() {

    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {

        //We listen to two intents.
        // The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
        } else {
            val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val state = when (stateStr) {
                TelephonyManager.EXTRA_STATE_IDLE -> TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK -> TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING ->  TelephonyManager.CALL_STATE_RINGING
                else -> 0
            }

            if((state == TelephonyManager.CALL_STATE_OFFHOOK || state == TelephonyManager.CALL_STATE_RINGING)
                && number == "644384296")
                endCall(context)
            else
                onCallStateChanged(context, state, number)
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected open fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {}
    protected open fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {}
    protected open fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {}
    protected open fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {}
    protected open fun onMissedCall(ctx: Context, number: String?, start: Date?) {}


    /**
     * End Call
     */
    private fun endCall(context: Context) {

        try {
            Timber.d("PhoneCallReceiver: End Call")
            // Java reflection to gain access to TelephonyManager's
            // ITelephony getter
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val c = Class.forName(tm.javaClass.name)
            val m = c.getDeclaredMethod("getITelephony")
            m.isAccessible = true
            val telephonyService = m.invoke(tm) as ITelephony
            telephonyService.endCall()
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("End Call Exception")
        }

    }

    /**
     * on Call State Changed
     */
    private fun onCallStateChanged(context: Context, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                onIncomingCallStarted(context, number, callStartTime!!)
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    onOutgoingCallStarted(context, savedNumber, callStartTime!!)
                }
            TelephonyManager.CALL_STATE_IDLE ->
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                when {
                    lastState == TelephonyManager.CALL_STATE_RINGING -> //Ring but no pickup-  a miss
                        onMissedCall(context, savedNumber, callStartTime)
                    isIncoming -> onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                    else -> onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }

    companion object {

        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming: Boolean = false
        private var savedNumber: String? = null  //because the passed incoming is only valid in ringing
    }
}