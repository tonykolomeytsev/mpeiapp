package kekmech.ru.feature_notes_impl.presentation.screen.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.addSystemVerticalPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.setResult
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.R
import kekmech.ru.feature_notes_impl.databinding.FragmentNoteEditBinding
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEffect
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Ui
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditState
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

internal class NoteEditFragment :
    Fragment(R.layout.fragment_note_edit),
    ElmRendererDelegate<NoteEditEffect, NoteEditState> {

    private val dependencies: NotesDependencies by inject()
    private val analytics by screenAnalytics("NoteEdit")
    private val adapter by fastLazy {
        BaseAdapter(NoteEditAdapterItem(::onNoteContentChanged))
    }
    private val formatter by fastLazy { PrettyDateFormatter(requireContext()) }
    private val viewBinding by viewBinding(FragmentNoteEditBinding::bind)
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    private val store by androidElmStore {
        dependencies.noteEditStoreFactory.create(
            note = getArgument(ARG_NOTE)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemVerticalPadding()
        viewBinding.apply {
            toolbar.setNavigationOnClickListener { close() }
            buttonSave.setOnClickListener {
                analytics.sendClick("SaveNote")
                store.accept(Ui.Click.SaveNote)
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
        viewBinding.textViewNoteDate.text =
            formatter.formatRelative(state.note.dateTime.toLocalDate())
    }

    override fun handleEffect(effect: NoteEditEffect) = when (effect) {
        is NoteEditEffect.CloseWithSuccess -> {
            close()
            setResult(resultKey, EmptyResult)
        }

        is NoteEditEffect.ShowError -> showBanner(Strings.something_went_wrong_error)
    }

    private fun onNoteContentChanged(content: String) {
        viewBinding.appBarLayout.isSelected = viewBinding.recyclerView.canScrollVertically(-1)
        store.accept(Ui.Action.NoteContentChanged(content))
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
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
