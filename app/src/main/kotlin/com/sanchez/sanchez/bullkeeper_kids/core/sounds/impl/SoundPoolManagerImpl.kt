package com.sanchez.sanchez.bullkeeper_kids.core.sounds.impl

import android.content.Context
import android.media.AudioAttributes
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import android.media.SoundPool
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RawRes
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager.Companion.EMERGENCY_SOUND

/**
 *
 */
class SoundPoolManagerImpl(
        context: Context
): ISoundManager {

    private val MAX_STREAMS = 2
    private val DEFAULT_SOUND = 1.0f

    /**
     * Sound Pool
     */
    private var soundPool: SoundPool

    /**
     * Sound Map
     */
    private var soundMap: Map<Int, Int>


    init {


        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val audioAttrib = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

            SoundPool.Builder().setAudioAttributes(audioAttrib)
                    .setMaxStreams(MAX_STREAMS).build()
        } else {

            SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        }


        // Sound Map
        soundMap = hashMapOf(
                Pair(EMERGENCY_SOUND, soundPool.load(context, EMERGENCY_SOUND, 1))
        )

    }

    /**
     * Play Sound
     */
    override fun playSound(@RawRes sound: Int) {
        if(soundMap.isNotEmpty() && soundMap.containsKey(sound))
            soundPool.play(soundMap[sound]!!, DEFAULT_SOUND, DEFAULT_SOUND,
                    1, 0, 1f)
    }
}