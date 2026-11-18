package com.pla.keyboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

class PlaKeyboardService : InputMethodService(), KeyboardViewCustom.KeyListener {

    private lateinit var prefs: PrefsManager
    private var keyboardView: KeyboardViewCustom? = null
    private var currentInputConnection: InputConnection? = null
    private var isShift = false
    private var isCapsLock = false

    override fun onCreate() {
        super.onCreate()
        prefs = PrefsManager(this)
    }

    override fun onCreateInputView(): View {
        keyboardView = KeyboardViewCustom(this).apply {
            listener = this@PlaKeyboardService
            prefsManager = this@PlaKeyboardService.prefs
            applySettings(prefs)
        }
        return keyboardView!!
    }

    override fun onStartInput(connection: InputConnection?, restarting: Boolean) {
        super.onStartInput(connection, restarting)
        currentInputConnection = connection
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        keyboardView?.applySettings(prefs)
    }

    override fun onKeyPressed(key: String) {
        val ic = currentInputConnection ?: return
        when (key) {
            "SHIFT" -> {
                if (isCapsLock) { isCapsLock = false; isShift = false }
                else if (isShift) { isCapsLock = true }
                else { isShift = true }
                keyboardView?.updateShiftState(isShift || isCapsLock)
            }
            "DEL" -> ic.deleteSurroundingText(1, 0)
            "SPACE" -> ic.commitText(" ", 1)
            "ENTER" -> {
                val et = currentInputEditorInfo?.inputType ?: 0
                if (et == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE)
                    ic.commitText("\n", 1)
                else
                    ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            "SYM" -> keyboardView?.toggleSymbols()
            else -> {
                val text = if (isShift || isCapsLock) key.uppercase() else key.lowercase()
                ic.commitText(text, 1)
                if (isShift && !isCapsLock) {
                    isShift = false
                    keyboardView?.updateShiftState(false)
                }
            }
        }
        HapticHelper.vibrate(this, prefs)
        SoundHelper.play(this, prefs)
    }
}