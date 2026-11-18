package com.pla.keyboard

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticHelper {
    fun vibrate(ctx: Context, prefs: PrefsManager) {
        if (prefs.getHapticLevel() <= 0) return
        
        // Safe cast (as?) mencegah crash jika HP tidak punya vibrator
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        if (vibrator != null && vibrator.hasVibrator()) {
            val amp = (prefs.getHapticLevel() * 255 / 100).coerceIn(1, 255)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(12, amp))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(12)
                }
            } catch (e: Exception) {
                // Abaikan jika getaran gagal
            }
        }
    }
}