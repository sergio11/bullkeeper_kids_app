package com.sanchez.sanchez.bullkeeper_kids.core.sounds

import androidx.annotation.RawRes
import com.sanchez.sanchez.bullkeeper_kids.R


/**
 * Sound Manager
 */
interface ISoundManager {

    companion object {
        val EMERGENCY_SOUND: Int
            get() = R.raw.emergency_alarm_002
    }

    /**
     * Play Sound
     */
    fun playSound(@RawRes sound: Int)
}