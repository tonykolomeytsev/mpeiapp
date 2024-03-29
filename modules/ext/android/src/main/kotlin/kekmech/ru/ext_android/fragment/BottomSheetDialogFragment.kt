package kekmech.ru.ext_android.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

open class BottomSheetDialogFragment(@LayoutRes val layoutResId: Int,) :
    com.google.android.material.bottomsheet.BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutResId, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            dialogInterface as BottomSheetDialog
            val view = dialogInterface
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (view != null) {
                view.setBackgroundColor(Color.TRANSPARENT)
                BottomSheetBehavior.from(view)
                    .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                        override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
                    })
            }
        }
        return dialog
    }
}
