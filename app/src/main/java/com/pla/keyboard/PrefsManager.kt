package com.pla.keyboard

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(ctx: Context) {
    private val sp: SharedPreferences =
        ctx.getSharedPreferences("pla_keyboard_prefs", Context.MODE_PRIVATE)

    fun getHeightDp(): Int = sp.getInt("height_dp", 260)
    fun setHeightDp(v: Int) = sp.edit().putInt("height_dp", v).apply()

    fun getHapticLevel(): Int = sp.getInt("haptic", 60)
    fun setHapticLevel(v: Int) = sp.edit().putInt("haptic", v).apply()

    fun getSoundVolume(): Int = sp.getInt("sound", 40)
    fun setSoundVolume(v: Int) = sp.edit().putInt("sound", v).apply()

    fun isDarkTheme(): Boolean = sp.getBoolean("dark", true)
    fun setDarkTheme(v: Boolean) = sp.edit().putBoolean("dark", v).apply()

    fun isDoubleTapMode(): Boolean = sp.getBoolean("double_tap", false)
    fun setDoubleTapMode(v: Boolean) = sp.edit().putBoolean("double_tap", v).apply()
}