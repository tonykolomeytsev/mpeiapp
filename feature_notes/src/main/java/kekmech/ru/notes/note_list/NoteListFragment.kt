package kekmech.ru.notes.note_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseBottomSheetDialogFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.notes.R
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.NoteEditFragment
import kekmech.ru.notes.note_list.mvi.NoteListEffect
import kekmech.ru.notes.note_list.mvi.NoteListEvent
import kekmech.ru.notes.note_list.mvi.NoteListEvent.Wish
import kekmech.ru.notes.note_list.mvi.NoteListFeature
import kekmech.ru.notes.note_list.mvi.NoteListState
import kotlinx.android.synthetic.main.fragment_note_list.*
import org.koin.android.ext.android.inject
import java.time.LocalDate

const val ARG_SELECTED_CLASSES = "Arg.SelectedClasses"
const val ARG_SELECTED_DATE = "Arg.SelectedDate"

class NoteListFragment : BaseBottomSheetDialogFragment<NoteListEvent, NoteListEffect, NoteListState, NoteListFeature>() {

    override val initEvent = Wish.Init

    private val dependencies by inject<NotesDependencies>()

    override fun createFeature() = dependencies.noteListFeatureFactory.create(
        selectedClasses = getArgument(ARG_SELECTED_CLASSES),
        selectedDate = getArgument(ARG_SELECTED_DATE)
    )

    override var layoutId = R.layout.fragment_note_list

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun render(state: NoteListState) {
        adapter.update(NoteListConverter(requireContext()).map(state))
    }

    override fun handleEffect(effect: NoteListEffect) = when (effect) {
        is NoteListEffect.ShowNoteLoadError -> Toast
            .makeText(requireContext(), R.string.something_went_wrong_error, Toast.LENGTH_SHORT).show()
        is NoteListEffect.OpenNoteEdit -> {
            addScreenForward {
                NoteEditFragment.newInstance(effect.note, effect.classes)
            }
        }
    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        SectionHeaderAdapterItem(),
        NoteAdapterItem(requireContext()),
        AddActionAdapterItem { feature.accept(Wish.Click.CreateNewNote) },
        SpaceAdapterItem()
    )

    companion object {
        fun newInstance(
            selectedClasses: Classes,
            selectedDate: LocalDate
        ) = NoteListFragment()
            .withArguments(
                ARG_SELECTED_CLASSES to selectedClasses,
                ARG_SELECTED_DATE to selectedDate
            )
    }
}