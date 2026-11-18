package com.pla.keyboard

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.pla.keyboard.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivitySettingsBinding
    private lateinit var prefs: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(b.root)
        prefs = PrefsManager(this)

        // Height
        b.seekHeight.max = 200
        b.seekHeight.progress = prefs.getHeightDp() - 160
        b.seekHeight.setOnSeekBarChangeListener(simple { p ->
            prefs.setHeightDp(160 + p)
            b.tvHeight.text = "Tinggi keyboard: ${160 + p} dp"
        })
        b.tvHeight.text = "Tinggi keyboard: ${prefs.getHeightDp()} dp"

        // Haptic
        b.seekHaptic.progress = prefs.getHapticLevel()
        b.seekHaptic.setOnSeekBarChangeListener(simple { p ->
            prefs.setHapticLevel(p)
            b.tvHaptic.text = "Getaran: $p%"
        })
        b.tvHaptic.text = "Getaran: ${prefs.getHapticLevel()}%"

        // Sound
        b.seekSound.progress = prefs.getSoundVolume()
        b.seekSound.setOnSeekBarChangeListener(simple { p ->
            prefs.setSoundVolume(p)
            b.tvSound.text = "Volume suara: $p%"
        })
        b.tvSound.text = "Volume suara: ${prefs.getSoundVolume()}%"

        // Theme
        b.switchDark.isChecked = prefs.isDarkTheme()
        b.switchDark.setOnCheckedChangeListener { _, v -> prefs.setDarkTheme(v) }

        // Accessibility
        b.switchDoubleTap.isChecked = prefs.isDoubleTapMode()
        b.switchDoubleTap.setOnCheckedChangeListener { _, v -> prefs.setDoubleTapMode(v) }

        // Tombol kembali
        b.btnBack.setOnClickListener { finish() }
    }

    private fun simple(cb: (Int) -> Unit) = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(sb: SeekBar?, p: Int, u: Boolean) = cb(p)
        override fun onStartTrackingTouch(sb: SeekBar?) {}
        override fun onStopTrackingTouch(sb: SeekBar?) {}
    }
}