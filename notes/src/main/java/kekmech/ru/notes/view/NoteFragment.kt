package kekmech.ru.notes.view

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import kekmech.ru.core.platform.BaseBottomSheetDialogFragment
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.core.usecases.IsDarkThemeEnabledUseCase
import kekmech.ru.coreui.Resources
import kekmech.ru.notes.NoteFragmentPresenter
import kekmech.ru.notes.R
import kotlinx.android.synthetic.main.fragment_note.*
import org.koin.android.ext.android.inject


class NoteFragment : BaseBottomSheetDialogFragment<NoteFragmentPresenter, NoteFragmentView>(
    layoutId = R.layout.fragment_note
), NoteFragmentView {

    override val presenter: NoteFragmentPresenter by inject()

    private val isDarkThemeEnabledUseCase: IsDarkThemeEnabledUseCase by inject()

    override var onBackNavClick: () -> Unit = {}

    override var onTextEdit: (String) -> Unit = {}

    override fun onResume() {
        super.onResume()
        editTextContent?.postDelayed({
            editTextContent?.requestFocus()
        }, 500)
        editTextContent?.addTextChangedListener {
            onTextEdit(it?.toString() ?: "")
            showSaved()
        }
    }

    override fun setStatus(coupleName: String, coupleDate: String) {
        textViewDisciplineName?.text = coupleName
        textViewDisciplineDate?.text = coupleDate
    }

    override fun setContent(coupleContent: String) {
        editTextContent?.setText(coupleContent)
    }

    override fun showSaved() {
        textViewSavingProgress?.alpha = 0.5f
        textViewSavingProgress?.animate()
            ?.alpha(0f)
            ?.setDuration(3000L)
            ?.start()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 && !isDarkThemeEnabledUseCase()) {
            setWhiteNavigationBar(dialog)
        }
        return dialog
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(@NonNull dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here

            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Resources.getColor(requireContext(), R.color.colorLightGray))

            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)

            window.setBackgroundDrawable(windowBackground)
        }
    }
}