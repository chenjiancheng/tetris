package com.example.android

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.example.common.logic.SoundType
import com.example.common.logic.SoundUtil

/**
 * 声音
 */
@SuppressLint("StaticFieldLeak")
class AndroidSoundUtil(private var context: Context?) :SoundUtil {

    private val sp: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private val map = mutableMapOf<Int, Int>()

    init {
        Sounds.forEachIndexed { index, soundType ->
            map[index] = sp.load(context, soundType, 1)
        }
    }


    fun release() {
        context = null
        sp.release()
    }


    override fun play(isMute: Boolean, sound: SoundType) {
        if (!isMute) {
            sp.play(requireNotNull(map[sound.res]), 1f, 1f, 0, 0, 1f)
        }
    }

}

val Sounds =
    listOf(R.raw.move, R.raw.rotate, R.raw.start, R.raw.drop, R.raw.clean)