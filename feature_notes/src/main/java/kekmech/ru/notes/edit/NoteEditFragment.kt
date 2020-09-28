package kekmech.ru.notes.edit

import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.*
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.mvi.NoteEditEffect
import kekmech.ru.notes.edit.mvi.NoteEditEvent
import kekmech.ru.notes.edit.mvi.NoteEditEvent.Wish
import kekmech.ru.notes.edit.mvi.NoteEditFeature
import kekmech.ru.notes.edit.mvi.NoteEditState
import kotlinx.android.synthetic.main.fragment_note_edit.*
import org.koin.android.ext.android.inject

private const val ARG_NOTE = "Arg.Note"

class NoteEditFragment : BaseFragment<NoteEditEvent, NoteEditEffect, NoteEditState, NoteEditFeature>() {

    override val initEvent = Wish.Init

    private val dependencies: NotesDependencies by inject()

    override fun createFeature() = dependencies.noteEditFeatureFactory.create(
        note = getArgument(ARG_NOTE)
    )

    override var layoutId: Int = R.layout.fragment_note_edit

    private val analytics: NoteEditAnalytics by inject()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        toolbar.init()
        editTextContent.showKeyboard()
        buttonSave.setOnClickListener {
            analytics.sendClick("SaveNote")
            feature.accept(Wish.Click.SaveNote(editTextContent.text.toString()))
        }
        analytics.sendScreenShown()
    }

    override fun render(state: NoteEditState) {
        editTextContent.setText(state.note.content)
    }

    override fun handleEffect(effect: NoteEditEffect) = when (effect) {
        is NoteEditEffect.CloseWithSuccess -> closeWithSuccess()
        is NoteEditEffect.ShowError -> showBanner(R.string.something_went_wrong_error)
    }

    companion object {

        fun newInstance(
            note: Note
        ) = NoteEditFragment()
            .withArguments(
                ARG_NOTE to note
            )
    }
}