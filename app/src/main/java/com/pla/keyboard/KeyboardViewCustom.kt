package com.pla.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

@SuppressLint("ViewConstructor")
class KeyboardViewCustom(context: Context) : LinearLayout(context) {

    interface KeyListener { fun onKeyPressed(key: String) }

    var listener: KeyListener? = null
    var prefsManager: PrefsManager? = null

    private var rowsContainer: LinearLayout
    private val letterRows = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")
    private val symbolRows = listOf("1234567890", "@#\$%&*()-", "+=\"/:;!?")
    private var symbolMode = false
    private var shiftActive = false

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.TRANSPARENT)
        rowsContainer = LinearLayout(context).apply { orientation = VERTICAL }
        addView(rowsContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        buildKeyboard()
    }

    fun applySettings(prefs: PrefsManager) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, prefs.getHeightDp().dp(context))
        requestLayout()
        rebuildColors(prefs)
    }

    private fun Int.dp(ctx: Context): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), ctx.resources.displayMetrics).toInt()

    private fun buildKeyboard() {
        rowsContainer.removeAllViews()
        val layout = if (symbolMode) symbolRows else letterRows
        layout.forEachIndexed { idx, chars ->
            val row = LinearLayout(context).apply {
                orientation = HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0).apply { weight = 1f }
            }
            if (idx == layout.lastIndex) addKey(row, "SHIFT", true)
            chars.forEach { addKey(row, it.toString(), false) }
            if (idx == layout.lastIndex) addKey(row, "DEL", true)
            rowsContainer.addView(row)
        }
        val bottom = LinearLayout(context).apply {
            orientation = HORIZONTAL; gravity = Gravity.CENTER
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0).apply { weight = 1f }
        }
        addKey(bottom, "SYM", true)
        addKey(bottom, "SPACE", true)
        addKey(bottom, "ENTER", true)
        rowsContainer.addView(bottom)
    }

    private fun addKey(parent: LinearLayout, label: String, wide: Boolean) {
        val display = when (label) {
            "SPACE" -> "␣"
            "SHIFT" -> if (shiftActive) "⬆" else "⇧"
            "DEL" -> "⌫"
            "ENTER" -> "⏎"
            "SYM" -> "?123"
            else -> if (shiftActive && label.length == 1) label.uppercase() else label
        }
        val tv = TextView(context).apply {
            text = display
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            isClickable = true
            isFocusable = true
            contentDescription = when (label) {
                "SPACE" -> "Spasi"
                "SHIFT" -> "Shift"
                "DEL" -> "Hapus"
                "ENTER" -> "Enter"
                "SYM" -> "Simbol"
                else -> "Huruf $label"
            }
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT).apply {
                weight = if (wide) 2f else 1f
                setMargins(4, 4, 4, 4)
            }
            setOnClickListener { listener?.onKeyPressed(label) }
        }
        parent.addView(tv)
    }

    fun updateShiftState(active: Boolean) {
        shiftActive = active
        buildKeyboard()
        prefsManager?.let { rebuildColors(it) }
    }

    fun toggleSymbols() {
        symbolMode = !symbolMode
        buildKeyboard()
        prefsManager?.let { rebuildColors(it) }
    }

    private fun rebuildColors(prefs: PrefsManager) {
        val dark = prefs.isDarkTheme()
        val bg = if (dark) Color.parseColor("#121212") else Color.parseColor("#EFEFEF")
        val key = if (dark) Color.parseColor("#2A2A2A") else Color.parseColor("#FFFFFF")
        val txt = if (dark) Color.WHITE else Color.BLACK
        setBackgroundColor(bg)
        childrenRecursive().forEach { v ->
            if (v is TextView) {
                v.setTextColor(txt)
                v.background = GradientDrawable().apply {
                    cornerRadius = 12f
                    setColor(key)
                    setStroke(1, if (dark) Color.parseColor("#3A3A3A") else Color.parseColor("#CCCCCC"))
                }
            }
        }
    }

    private fun ViewGroup.childrenRecursive(): List<View> {
        val list = mutableListOf<View>()
        for (i in 0 until childCount) {
            val c = getChildAt(i)
            list.add(c)
            if (c is ViewGroup) list.addAll((c as ViewGroup).childrenRecursive())
        }
        return list
    }
}