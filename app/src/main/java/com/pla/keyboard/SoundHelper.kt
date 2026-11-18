package com.pla.keyboard

import android.content.Context
import android.media.SoundPool

object SoundHelper {
    private var soundPool: SoundPool? = null
    private var soundId = 0
    private var volume = 0.4f

    fun play(ctx: Context, prefs: PrefsManager) {
        volume = prefs.getSoundVolume() / 100f
        if (volume <= 0f) return
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setMaxStreams(4).build()
            val id = ctx.resources.getIdentifier("keypress", "raw", ctx.packageName)
            if (id != 0) soundId = soundPool!!.load(ctx, id, 1)
        }
        if (soundId != 0) soundPool?.play(soundId, volume, volume, 1, 0, 1f)
    }
}