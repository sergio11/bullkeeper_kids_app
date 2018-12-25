package com.sanchez.sanchez.bullkeeper_kids.core.sounds

import androidx.annotation.RawRes
import com.sanchez.sanchez.bullkeeper_kids.R


/**
 * Sound Manager
 */
interface ISoundManager {

    /**
     * Sound Resources
     */
    companion object {
        val APP_BLOCKED_SOUND: Int
            get() = R.raw.app_blocked
        val SOS_ALARM_SOUND: Int
            get() = R.raw.sos_alarm
    }

    /**
     * Play Sound
     */
    fun playSound(@RawRes sound: Int): Int

    /**
     * Play Sound
     */
    fun playSound(@RawRes sound: Int, loop: Boolean): Int

    /**
     * Stop Sound
     */
    fun stopSound(streamId: Int)
}