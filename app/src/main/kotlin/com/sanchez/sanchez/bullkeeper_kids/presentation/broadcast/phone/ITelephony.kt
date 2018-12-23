package com.android.internal.telephony

/**
 * ITelephony
 */
interface ITelephony {
    fun endCall(): Boolean
    fun answerRingingCall()
    fun silenceRinger()
}