package kekmech.ru.notes.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.databinding.FragmentNoteEditBinding
import kekmech.ru.notes.di.NotesDependencies
import kekmech.ru.notes.edit.elm.NoteEditEffect
import kekmech.ru.notes.edit.elm.NoteEditEvent
import kekmech.ru.notes.edit.elm.NoteEditEvent.Wish
import kekmech.ru.notes.edit.elm.NoteEditState
import org.koin.android.ext.android.inject

internal class NoteEditFragment : BaseFragment<NoteEditEvent, NoteEditEffect, NoteEditState>() {

    override val initEvent = Wish.Init
    override var layoutId: Int = R.layout.fragment_note_edit

    private val dependencies: NotesDependencies by inject()
    private val analytics by screenAnalytics("NoteEdit")
    private val adapter by fastLazy {
        BaseAdapter(NoteEditAdapterItem(::onNoteContentChanged))
    }
    private val formatter by fastLazy { PrettyDateFormatter(requireContext()) }
    private val viewBinding by viewBinding(FragmentNoteEditBinding::bind)
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    override fun createStore() = dependencies.noteEditFeatureFactory.create(
        note = getArgument(ARG_NOTE)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemVerticalPadding()
        viewBinding.apply {
            toolbar.setNavigationOnClickListener { close() }
            buttonSave.setOnClickListener {
                analytics.sendClick("SaveNote")
                feature.accept(Wish.Click.SaveNote)
            }
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            recyclerView.adapter = adapter
            recyclerView.itemAnimator = null
            recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        }
    }

    override fun render(state: NoteEditState) {
        if (adapter.allData.isEmpty())
            adapter.update(listOf(state.note))
        viewBinding.textViewNoteDiscipline.text = state.note.classesName
        viewBinding.textViewNoteDate.text = formatter.formatRelative(state.note.dateTime.toLocalDate())
    }

    override fun handleEffect(effect: NoteEditEffect) = when (effect) {
        is NoteEditEffect.CloseWithSuccess -> {
            close()
            setResult(resultKey, EmptyResult)
        }
        is NoteEditEffect.ShowError -> showBanner(R.string.something_went_wrong_error)
    }

    private fun onNoteContentChanged(content: String) {
        viewBinding.appBarLayout.isSelected = viewBinding.recyclerView.canScrollVertically(-1)
        feature.accept(Wish.Action.NoteContentChanged(content))
    }

    companion object {

        private const val ARG_NOTE = "Arg.Note"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            note: Note,
            resultKey: String,
        ) = NoteEditFragment()
            .withArguments(
                ARG_NOTE to note,
                ARG_RESULT_KEY to resultKey
            )
    }
}