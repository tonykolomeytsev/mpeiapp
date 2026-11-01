package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.removeArg
import kekmech.ru.ext_android.serializableArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Ui
import kekmech.ru.feature_notes_impl.presentation.screen.edit.NoteEditFragment
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

internal class AllNotesFragment : ComposeFragment() {

    private val dependencies: NotesDependencies by inject()
    private val analytics by screenAnalytics("AllNotes")

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            // Selected note is shown only once, so the screen doesn't scroll back to it after recreation
            var selectedNote by remember { mutableStateOf(serializableArg<Note>(ARG_SELECTED_NOTE)) }
            ElmContent(
                key = "AllNotesScreen",
                storeFactory = { dependencies.allNotesStoreFactory.create() },
            ) { onAccept, state, _ ->
                ResultListener<EmptyResult>(NOTE_EDIT_RESULT_KEY) {
                    onAccept(Ui.Init)
                }
                AllNotesScreen(
                    notes = state.notes,
                    selectedNote = selectedNote,
                    onBackClick = { router.executeCommand(PopBackStack()) },
                    onSearchClick = {
                        analytics.sendClick("SearchNotes")
                        dependencies.searchFeatureLauncher.launch(
                            query = "",
                            filter = "NOTES",
                        )
                    },
                    onNoteClick = { note ->
                        analytics.sendClick("EditNote")
                        router.executeCommand(AddScreenForward {
                            NoteEditFragment.newInstance(note, NOTE_EDIT_RESULT_KEY)
                        })
                    },
                    onNoteDelete = { note ->
                        analytics.sendClick("DeleteNote")
                        onAccept(Ui.Action.DeleteNote(note))
                    },
                    onSelectedNoteShown = {
                        removeArg(ARG_SELECTED_NOTE)
                        selectedNote = null
                    },
                )
            }
        }
    }

    companion object {

        private const val ARG_SELECTED_NOTE = "Arg.Note"
        private const val NOTE_EDIT_RESULT_KEY = "NOTE_EDIT_RESULT_KEY"

        fun newInstance(selectedNote: Note?) = AllNotesFragment()
            .withArguments(ARG_SELECTED_NOTE to selectedNote)
    }
}
