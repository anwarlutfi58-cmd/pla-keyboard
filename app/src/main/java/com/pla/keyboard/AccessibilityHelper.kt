package com.pla.keyboard

import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityManager

object AccessibilityHelper {
    fun isScreenReaderOn(ctx: Context): Boolean {
        val am = ctx.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled && am.isTouchExplorationEnabled
    }

    fun applyTapMode(view: View, doubleTapMode: Boolean) {
        view.isFocusable = true
        view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        view.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
        if (view.contentDescription.isNullOrBlank()) {
            view.contentDescription = ""
        }
    }
}