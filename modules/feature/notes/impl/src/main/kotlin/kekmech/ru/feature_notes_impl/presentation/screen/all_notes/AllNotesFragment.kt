package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.addSystemVerticalPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.findAndRemoveArgument
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.R
import kekmech.ru.feature_notes_impl.databinding.FragmentAllNotesBinding
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEffect
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Ui
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesState
import kekmech.ru.feature_notes_impl.presentation.screen.edit.NoteEditFragment
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.addScrollAnalytics
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.addScreenForward
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject

internal class AllNotesFragment :
    Fragment(R.layout.fragment_all_notes),
    ElmRendererDelegate<AllNotesEffect, AllNotesState> {

    private val dependencies: NotesDependencies by inject()
    private val analytics by screenAnalytics("AllNotes")
    private val adapter by fastLazy { createAdapter() }
    private val viewBinding by viewBinding(FragmentAllNotesBinding::bind)

    private val store by androidElmStore { dependencies.allNotesStoreFactory.create() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemVerticalPadding()
        viewBinding.apply {
            toolbar.setNavigationOnClickListener { close() }
            toolbar.setOnMenuItemClickListener {
                analytics.sendClick("SearchNotes")
                searchNotes()
                true
            }
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
            recyclerView.addScrollAnalytics(analytics, "RecyclerView")
            recyclerView.attachSwipeToDeleteCallback(isItemForDelete = { it is Note }) { note ->
                analytics.sendClick("DeleteNote")
                store.accept(Ui.Action.DeleteNote(note as Note))
            }
        }
    }

    override fun render(state: AllNotesState) {
        adapter.update(AllNotesListConverter().map(state))
        scrollToSelectedNote(state)
    }

    private fun scrollToSelectedNote(state: AllNotesState) {
        if (!state.notes.isNullOrEmpty()) {
            findAndRemoveArgument<Note>(ARG_SELECTED_NOTE)?.let { note ->
                val position = adapter.allData.indexOf(note)
                if (position != -1) {
                    viewBinding.recyclerView.scrollToPosition(position)
                }
            }
        }
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        NoteAdapterItem(requireContext()) {
            analytics.sendClick("EditNote")
            navigateToNoteEdit(it)
        },
        SectionHeaderAdapterItem(),
        ShimmerAdapterItem(R.layout.item_note_shimmer),
        EmptyStateAdapterItem()
    )

    private fun navigateToNoteEdit(note: Note) {
        addScreenForward {
            NoteEditFragment.newInstance(note, NOTE_EDIT_RESULT_KEY)
        }
        setResultListener<EmptyResult>(NOTE_EDIT_RESULT_KEY) {
            store.accept(Ui.Init)
        }
    }

    private fun searchNotes() {
        dependencies.searchFeatureLauncher.launch(
            query = "",
            filter = "NOTES"
        )
    }

    companion object {

        private const val ARG_SELECTED_NOTE = "Arg.Note"
        private const val NOTE_EDIT_RESULT_KEY = "NOTE_EDIT_RESULT_KEY"

        fun newInstance(selectedNote: Note?) = AllNotesFragment()
            .withArguments(ARG_SELECTED_NOTE to selectedNote)
    }
}
