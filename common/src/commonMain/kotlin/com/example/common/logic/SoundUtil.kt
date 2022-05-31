package com.example.common.logic

interface SoundUtil {
    fun play(b: Boolean, sound: SoundType) {}
}
sealed class SoundType(val res: Int) {
    object Move : SoundType(0)
    object Rotate : SoundType(1)
    object Start : SoundType(2)
    object Drop : SoundType(3)
    object Clean : SoundType(4)
}