package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.phone

import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.CallReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IPhoneNumberRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ScheduledBlocksRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
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
     * Synchronize Terminal Call History Interact
     */
    @Inject
    internal lateinit var synchronizeTerminalCallHistoryInteract: SynchronizeTerminalCallHistoryInteract

    /**
     * Phone Number Blocked Repository
     */
    @Inject
    internal lateinit var phoneNumberBlockedRepository: IPhoneNumberRepository

    /**
     * Scheduled Block Repository Impl
     */
    @Inject
    internal lateinit var scheduledBlocksRepositoryImpl: ScheduledBlocksRepositoryImpl

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator


    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {
        callReceiverComponent.inject(this)
        super.onReceive(context, intent)
    }

    /**
     * Check Phone Number
     */
    private fun checkPhoneNumber(ctx: Context, phoneNumber: String) {
        Timber.d("CallReceiver: Check Phone Number")

        val scheduledBlockEnable = scheduledBlocksRepositoryImpl.getScheduledBlockEnableForThisMoment(
                ctx.getString(R.string.joda_local_time_format_server_response)
        )

        // Check All Calls
        if(scheduledBlockEnable?.allowCalls != null
            && !scheduledBlockEnable.allowCalls!!) {

            // End Call
            endCall(ctx)

            // Show Block Scheduled Screen
            navigator.showScheduledBlockActive(ctx, scheduledBlockEnable.name,
                    scheduledBlockEnable.image, scheduledBlockEnable.startAt, scheduledBlockEnable.endAt,
                    scheduledBlockEnable.description)

        } else {

            phoneNumberBlockedRepository.findByPhoneNumber(phoneNumber)?.let {
                Timber.d("CallReceiver: Phone Number to lock -> %s at -> %s", it.phoneNumber, it.blockedAt)
                // End Call
                endCall(ctx)
                // Show Phone Blocked Screen
                if(!it.phoneNumber.isNullOrEmpty() && !it.blockedAt.isNullOrEmpty())
                    navigator.showPhoneNumberBlockedScreen(ctx, it.phoneNumber!!, it.blockedAt!!)
            }

        }



    }

    /**
     * End Cann For Unknown Emitter
     */
    private fun endCallForUnknownEmitter(ctx: Context) {
        // End Call
        endCall(ctx)
        // Show Phone Number Blocked Screen
        navigator.showPhoneNumberBlockedScreen(ctx)
    }

    /**
     * On Incoming Call Started
     */
    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onIncomingCallStarted(ctx, number, start)
        Timber.d("CallReceiver: onIncomingCallStarted -> %s", number)
        if(!number.isNullOrEmpty())
            checkPhoneNumber(ctx, number)
        else
            endCallForUnknownEmitter(ctx)
    }

    /**
     * On Outgoing Call Started
     */
    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onOutgoingCallStarted(ctx, number, start)
        Timber.d("CallReceiver: onOutgoingCallStarted -> %s", number)
        number?.let { checkPhoneNumber(ctx, it) }
    }

    /**
     * On Incoming Call Ended
     */
    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onIncomingCallEnded(ctx, number, start, end)
        Timber.d("CallReceiver: onIncomingCallEnded -> %s", number)
        syncTerminalCallHistory()
    }

    /**
     * On Outgoing Call Ended
     */
    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onOutgoingCallEnded(ctx, number, start, end)
        Timber.d("CallReceiver: onOutgoingCallEnded -> %s", number)
        syncTerminalCallHistory()
    }

    /**
     * On Missed Call
     */
    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
        super.onMissedCall(ctx, number, start)
        Timber.d("CallReceiver: onMissedCall -> %s", number)
        syncTerminalCallHistory()
    }

    /**
     * Sync Terminal Call History
     */
    private fun syncTerminalCallHistory(){
        synchronizeTerminalCallHistoryInteract(UseCase.None()){
            it.either(::onSynchronizeTerminalCallHistoryFailed,
                    ::onSynchronizeTerminalCallHistorySuccess)
        }
    }

    /**
     * On Synchronize Terminal Call History Failed
     */
    private fun onSynchronizeTerminalCallHistoryFailed(failure: Failure) {
        Timber.d("CallReceiver: Sync Failed")
    }


    private fun onSynchronizeTerminalCallHistorySuccess(total: Int) {
        Timber.d("CallReceiver: Total calls sync -> %d", total)
    }

}