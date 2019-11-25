package kekmech.ru.notes.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.notes.NoteFragmentPresenter
import kekmech.ru.notes.R
import kotlinx.android.synthetic.main.fragment_note.*
import javax.inject.Inject
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener


class NoteFragment : BaseFragment<NoteFragmentPresenter, NoteFragmentView>(
    layoutId = R.layout.fragment_note
), NoteFragmentView {

    @Inject
    override lateinit var presenter: NoteFragmentPresenter

    override var onBackNavClick: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationOnClickListener { onBackNavClick() }
        editTextContent?.post {
            editTextContent?.requestFocus()
        }
        textViewDisciplineName?.text = "Гидропневмоприводы мехатронных и робототехнических устройств"
        textViewDisciplineDate?.text = "15 неделя, среда, 3 пара"
        editTextContent?.addTextChangedListener {
            showSaved()
        }
    }

    override fun showSaved() {
        textViewSavingProgress?.alpha = 0.5f
        textViewSavingProgress?.animate()
            ?.alpha(0f)
            ?.setDuration(5000L)
            ?.start()
    }
}