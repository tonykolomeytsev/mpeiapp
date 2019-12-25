package kekmech.ru.notes.view

import androidx.core.widget.addTextChangedListener
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.notes.NoteFragmentPresenter
import kekmech.ru.notes.R
import kotlinx.android.synthetic.main.fragment_note.*
import org.koin.android.ext.android.inject


class NoteFragment : BaseFragment<NoteFragmentPresenter, NoteFragmentView>(
    layoutId = R.layout.fragment_note
), NoteFragmentView {

    override val presenter: NoteFragmentPresenter by inject()

    override var onBackNavClick: () -> Unit = {}

    override var onTextEdit: (String) -> Unit = {}

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationOnClickListener { onBackNavClick() }
        editTextContent?.post {
            editTextContent?.requestFocus()
        }
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
}