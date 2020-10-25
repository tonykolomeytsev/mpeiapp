package kekmech.ru.notes.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.closeWithSuccess
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
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

internal class NoteEditFragment : BaseFragment<NoteEditEvent, NoteEditEffect, NoteEditState, NoteEditFeature>() {

    override val initEvent = Wish.Init

    private val dependencies: NotesDependencies by inject()

    override fun createFeature() = dependencies.noteEditFeatureFactory.create(
        note = getArgument(ARG_NOTE)
    )

    override var layoutId: Int = R.layout.fragment_note_edit

    private val analytics: NoteEditAnalytics by inject()

    private val adapter by fastLazy {
        BaseAdapter(NoteEditAdapterItem(::onNoteContentChanged))
    }

    private val formatter by fastLazy { PrettyDateFormatter(requireContext()) }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        toolbar.init()
        buttonSave.setOnClickListener {
            analytics.sendClick("SaveNote")
            feature.accept(Wish.Click.SaveNote)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        analytics.sendScreenShown()
    }

    override fun render(state: NoteEditState) {
        if (adapter.allData.isEmpty())
            adapter.update(listOf(state.note))
        textViewNoteDiscipline.text = state.note.classesName
        textViewNoteDate.text = formatter.formatRelative(state.note.dateTime.toLocalDate())
    }

    override fun handleEffect(effect: NoteEditEffect) = when (effect) {
        is NoteEditEffect.CloseWithSuccess -> closeWithSuccess()
        is NoteEditEffect.ShowError -> showBanner(R.string.something_went_wrong_error)
    }

    private fun onNoteContentChanged(content: String) {
        appBarLayout.isSelected = recyclerView.canScrollVertically(-1)
        feature.accept(Wish.Action.NoteContentChanged(content))
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