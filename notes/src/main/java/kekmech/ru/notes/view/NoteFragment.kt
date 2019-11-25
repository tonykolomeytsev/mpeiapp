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
    }

    fun showKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInput(SHOW_FORCED, 0)
    }

    fun closeKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInput(HIDE_IMPLICIT_ONLY, 0)
    }
}