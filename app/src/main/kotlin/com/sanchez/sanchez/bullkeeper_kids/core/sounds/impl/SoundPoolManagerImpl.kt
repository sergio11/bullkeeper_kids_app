package com.sanchez.sanchez.bullkeeper_kids.core.sounds.impl

import android.content.Context
import android.media.AudioAttributes
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import android.media.SoundPool
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RawRes
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager.Companion.APP_BLOCKED_SOUND
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager.Companion.SOS_ALARM_SOUND
import timber.log.Timber

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
                Pair(APP_BLOCKED_SOUND, soundPool.load(context, APP_BLOCKED_SOUND, 1)),
                Pair(SOS_ALARM_SOUND, soundPool.load(context, SOS_ALARM_SOUND, 1))
        )

    }

    /**
     * Play Sound
     */
    override fun playSound(@RawRes sound: Int): Int =  playSound(sound, false)

    /**
     * Play Sound
     */
    override fun playSound(@RawRes sound: Int, loop: Boolean): Int {
        var streamId: Int = -1
        if(soundMap.isNotEmpty() && soundMap.containsKey(sound)) {
            Timber.d("SOUND: Play Sound")
            streamId = soundPool.play(soundMap[sound]!!, DEFAULT_SOUND, DEFAULT_SOUND,
                    1, if(loop) -1 else 0, 1f)
        }
        return streamId
    }

    /**
     * Stop Sound
     */
    override fun stopSound(streamId: Int) {
        if(streamId != -1)
            soundPool.stop(streamId)
    }
}