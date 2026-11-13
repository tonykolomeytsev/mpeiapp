package kekmech.ru.feature_notes_impl.presentation.screen.note_list

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.navigation.NoteEditScreenKey
import kekmech.ru.feature_notes_impl.navigation.NoteListScreenKey
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEffect
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEvent.Ui
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.ReplaceScreen
import org.koin.compose.koinInject
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun NoteListEntry(key: NoteListScreenKey) {
    val dependencies = koinInject<NotesDependencies>()
    val analytics = rememberScreenAnalytics("NoteList")
    val router = LocalRouter.current
    val context = LocalContext.current
    ElmContent(
        key = "NoteListScreen",
        storeFactory = {
            dependencies.noteListStoreFactory.create(
                selectedClasses = key.selectedClasses,
                selectedDate = key.selectedDate,
            )
        },
    ) { onAccept, state, effects ->
        NoteListScreen(
            notes = state.notes,
            onNoteClick = { note ->
                analytics.sendClick("EditNote")
                onAccept(Ui.Click.EditNote(note))
            },
            onNoteDelete = { note ->
                analytics.sendClick("DeleteNote")
                onAccept(Ui.Action.DeleteNote(note))
            },
            onCreateNoteClick = {
                analytics.sendClick("NewNote")
                onAccept(Ui.Click.CreateNewNote)
            },
        )
        EffectHandler(effects) { effect ->
            when (effect) {
                is NoteListEffect.ShowNoteLoadError -> Toast
                    .makeText(context, Strings.something_went_wrong_error, Toast.LENGTH_SHORT)
                    .show()

                // The sheet is replaced, so going back from the note editor
                // returns to the screen below the sheet, as before
                is NoteListEffect.OpenNoteEdit -> router.executeCommand(
                    ReplaceScreen(NoteEditScreenKey(effect.note, key.resultKey))
                )
            }
        }
    }
}
