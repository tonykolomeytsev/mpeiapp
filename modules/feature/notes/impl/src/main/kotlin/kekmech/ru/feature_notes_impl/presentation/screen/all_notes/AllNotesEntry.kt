package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.navigation.AllNotesScreenKey
import kekmech.ru.feature_notes_impl.navigation.NoteEditScreenKey
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesEvent.Ui
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

private const val NOTE_EDIT_RESULT_KEY = "NOTE_EDIT_RESULT_KEY"

@Composable
internal fun AllNotesEntry(key: AllNotesScreenKey) {
    val dependencies = koinInject<NotesDependencies>()
    val analytics = rememberScreenAnalytics("AllNotes")
    MpeixTheme {
        val router = LocalRouter.current
        // Selected note is shown only once, so the screen doesn't scroll back to it after recreation
        var selectedNote by rememberSaveable { mutableStateOf(key.selectedNote) }
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
                    router.executeCommand(AddScreenForward(NoteEditScreenKey(note, NOTE_EDIT_RESULT_KEY)))
                },
                onNoteDelete = { note ->
                    analytics.sendClick("DeleteNote")
                    onAccept(Ui.Action.DeleteNote(note))
                },
                onSelectedNoteShown = { selectedNote = null },
            )
        }
    }
}
