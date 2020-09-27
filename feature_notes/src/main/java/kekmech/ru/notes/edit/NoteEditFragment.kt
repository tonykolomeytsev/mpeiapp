package kekmech.ru.notes.edit

import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.notes.R
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.mvi.NoteEditEffect
import kekmech.ru.notes.edit.mvi.NoteEditEvent
import kekmech.ru.notes.edit.mvi.NoteEditEvent.Wish
import kekmech.ru.notes.edit.mvi.NoteEditFeature
import kekmech.ru.notes.edit.mvi.NoteEditState
import org.koin.android.ext.android.inject

private const val ARG_NOTE = "Arg.Note"
private const val ARG_CLASSES = "Arg.Classes"

class NoteEditFragment : BaseFragment<NoteEditEvent, NoteEditEffect, NoteEditState, NoteEditFeature>() {

    override val initEvent = Wish.Init

    private val dependencies: NotesDependencies by inject()

    override fun createFeature() = dependencies.noteEditFeatureFactory.create(
        note = getArgument(ARG_NOTE),
        classes = getArgument(ARG_CLASSES)
    )

    override var layoutId: Int = R.layout.fragment_note_edit

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {

    }

    override fun render(state: NoteEditState) {

    }

    override fun handleEffect(effect: NoteEditEffect) {

    }

    companion object {

        fun newInstance(
            note: Note,
            classes: Classes
        ) = NoteEditFragment()
            .withArguments(
                ARG_NOTE to note,
                ARG_CLASSES to classes
            )
    }
}