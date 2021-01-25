package kekmech.ru.notes.all_notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.*
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.all_notes.mvi.AllNotesEffect
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent.Wish
import kekmech.ru.notes.all_notes.mvi.AllNotesFeature
import kekmech.ru.notes.all_notes.mvi.AllNotesState
import kekmech.ru.notes.databinding.FragmentAllNotesBinding
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.NoteEditFragment
import org.koin.android.ext.android.inject
import ru.kekmech.common_images.launcher.ImagePickerLauncher

private const val NOTE_EDIT_REQUEST_CODE = 54692

private const val ARG_SELECTED_NOTE = "Arg.Note"

internal class AllNotesFragment : BaseFragment<AllNotesEvent, AllNotesEffect, AllNotesState, AllNotesFeature>(), ActivityResultListener {

    override val initEvent = Wish.Init

    private val dependencies: NotesDependencies by inject()

    override fun createFeature() = dependencies.allNotesFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_all_notes

    private val analytics: AllNotesAnalytics by inject()

    private val adapter by fastLazy { createAdapter() }

    private val viewBinding by viewBinding(FragmentAllNotesBinding::bind)

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        viewBinding.apply {
            toolbar.init()
            toolbar.setOnMenuItemClickListener { searchNotes(); true }
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
            recyclerView.addScrollAnalytics(analytics, "RecyclerView")
            recyclerView.attachSwipeToDeleteCallback(isItemForDelete = { it is Note }) { note ->
                analytics.sendClick("DeleteNote")
                feature.accept(Wish.Action.DeleteNote(note as Note))
            }
        }
        analytics.sendScreenShown()
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
        ShimmerAdapterItem(0, R.layout.item_note_shimmer),
        EmptyStateAdapterItem(),
        AddActionAdapterItem {
            inject<ImagePickerLauncher>().value.launch()
        }
    )

    private fun navigateToNoteEdit(note: Note) = addScreenForward {
        NoteEditFragment.newInstance(note)
            .withResultFor(this, NOTE_EDIT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NOTE_EDIT_REQUEST_CODE) {
            feature.accept(Wish.Init)
        }
    }

    private fun searchNotes() {
        dependencies.searchFeatureLauncher.launch(
            query = "",
            filter = "NOTES"
        )
    }

    companion object {

        fun newInstance(selectedNote: Note?) = AllNotesFragment()
            .withArguments(ARG_SELECTED_NOTE to selectedNote)
    }
}