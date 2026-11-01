package com.kekmech.lib_fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.fragment.compose.content
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.ext_android.R as ExtAndroidR

/**
 * Compose Bottom Sheet, который не требует XML layout.
 * Открывается через `ShowDialog` так же, как и XML bottom sheet'ы.
 *
 * Фон, скругления и drag handle рисует сам экран,
 * для этого используй `BottomSheet` из `ui_kit_dialogs`.
 */
public abstract class ComposeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    // Живет вместе с фрагментом, а не с view, чтобы не пересоздаваться при каждой композиции
    private val resultController by lazy(LazyThreadSafetyMode.NONE) {
        FragmentResultController(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), ExtAndroidR.style.BottomSheetDialogTheme).apply {
            behavior.skipCollapsed = true
            setOnShowListener {
                // Высота compose контента известна только после первого layout'а,
                // поэтому без явного EXPANDED шторка может открыться наполовину
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.setBackgroundColor(Color.TRANSPARENT)
            }
        }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = content {
        ProvideScreenDependencies(resultController) {
            Screen()
        }
    }

    @Composable
    public abstract fun Screen()
}
