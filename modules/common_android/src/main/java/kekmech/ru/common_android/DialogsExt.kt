package kekmech.ru.common_android

import android.app.Dialog
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun Dialog.setExpandedBottomSheet(): Dialog = apply {
    setOnShowListener {
        val bottomSheet = getBottomSheetFrame()
        setBottomSheetStateExpanded(bottomSheet)
    }
}

fun Dialog.setFullHeightBottomSheet(): Dialog = apply {
    setOnShowListener {
        val bottomSheet = getBottomSheetFrame()
        val lp = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = CoordinatorLayout.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = lp
        setBottomSheetStateExpanded(bottomSheet)
    }
}

fun Dialog.getBottomSheetFrame(): FrameLayout =
    findViewById(com.google.android.material.R.id.design_bottom_sheet)

private fun setBottomSheetStateExpanded(bottomSheet: FrameLayout) {
    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
}
