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
        val PICK_ME_UP_SOUND: Int
            get() = R.raw.pick_me_up_sound
        val TIME_BANK_SOUND: Int
            get() = R.raw.time_bank_sound
        val BED_TIME_SOUND: Int
            get() = R.raw.bed_time_sound
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