package kekmech.ru.notes.note_list

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.close
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseBottomSheetDialogFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.coreui.items.*
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.notes.R
import kekmech.ru.notes.databinding.FragmentNoteListBinding
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.NoteEditFragment
import kekmech.ru.notes.note_list.elm.NoteListEffect
import kekmech.ru.notes.note_list.elm.NoteListEvent
import kekmech.ru.notes.note_list.elm.NoteListEvent.Wish
import kekmech.ru.notes.note_list.elm.NoteListState
import org.koin.android.ext.android.inject
import java.time.LocalDate

internal class NoteListFragment :
    BaseBottomSheetDialogFragment<NoteListEvent, NoteListEffect, NoteListState>() {

    override val initEvent = Wish.Init
    override val layoutId = R.layout.fragment_note_list

    private val dependencies by inject<NotesDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("NoteList")
    private val viewBinding by viewBinding(FragmentNoteListBinding::bind)
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }
    private val listConverter by fastLazy { NoteListConverter(requireContext()) }

    override fun createStore() = dependencies.noteListFeatureFactory.create(
        selectedClasses = getArgument(ARG_SELECTED_CLASSES),
        selectedDate = getArgument(ARG_SELECTED_DATE)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.attachSwipeToDeleteCallback(isItemForDelete = { it is Note }) { note ->
                analytics.sendClick("DeleteNote")
                feature.accept(Wish.Action.DeleteNote(note as Note))
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        close()
    }

    override fun render(state: NoteListState) {
        adapter.update(listConverter.map(state))
    }

    override fun handleEffect(effect: NoteListEffect) = when (effect) {
        is NoteListEffect.ShowNoteLoadError -> Toast
            .makeText(requireContext(), R.string.something_went_wrong_error, Toast.LENGTH_SHORT).show()
        is NoteListEffect.OpenNoteEdit -> {
            close()
            addScreenForward { NoteEditFragment.newInstance(effect.note, resultKey) }
        }
    }

    private fun createAdapter() = BaseAdapter(
        PullAdapterItem(),
        SectionHeaderAdapterItem(),
        NoteAdapterItem(requireContext()) {
            analytics.sendClick("EditNote")
            feature.accept(Wish.Click.EditNote(it))
        },
        AddActionAdapterItem {
            analytics.sendClick("NewNote")
            feature.accept(Wish.Click.CreateNewNote)
        },
        SpaceAdapterItem()
    )

    companion object {

        private const val ARG_SELECTED_CLASSES = "Arg.SelectedClasses"
        private const val ARG_SELECTED_DATE = "Arg.SelectedDate"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            selectedClasses: Classes,
            selectedDate: LocalDate,
            resultKey: String,
        ): BottomSheetDialogFragment = NoteListFragment()
            .withArguments(
                ARG_SELECTED_CLASSES to selectedClasses,
                ARG_SELECTED_DATE to selectedDate,
                ARG_RESULT_KEY to resultKey
            )
    }
}