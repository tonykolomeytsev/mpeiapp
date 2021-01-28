package ru.kekmech.common_images.imagepicker.utils

import android.graphics.Rect
import android.view.View
import android.view.Window

class BottomSheetFixedElementHelper(
    private val fixedElement: View,
    private val window: Window
) {
    private val bottomSheetRect = Rect(0, 0, 0, 0)
    private val statusBarHeight = getStatusBarHeight()

    private fun getStatusBarHeight(): Int {
        val r = Rect()
        window.decorView.getWindowVisibleDisplayFrame(r)
        return r.top
    }

    fun update(bottomSheet: View) {
        bottomSheet.getGlobalVisibleRect(bottomSheetRect)
        fixedElement.translationY = (statusBarHeight - bottomSheetRect.top).toFloat()
    }
}