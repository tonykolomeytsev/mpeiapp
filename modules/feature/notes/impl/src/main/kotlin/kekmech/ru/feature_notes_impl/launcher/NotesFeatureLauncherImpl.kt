package kekmech.ru.feature_notes_impl.launcher

import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.AllNotesFragment
import kekmech.ru.feature_notes_impl.presentation.screen.edit.NoteEditFragment
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.NoteListFragment
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.library_navigation.AddScreenForward
import kekmech.ru.library_navigation.Router
import kekmech.ru.library_navigation.ShowDialog
import java.time.LocalDate

internal class NotesFeatureLauncherImpl(
    private val router: Router,
) : NotesFeatureLauncher {

    override fun launchNoteList(
        selectedClasses: Classes,
        selectedDate: LocalDate,
        resultKey: String,
    ) {
        router.executeCommand(
            ShowDialog {
                NoteListFragment.newInstance(selectedClasses, selectedDate, resultKey)
            }
        )
    }

    override fun launchNoteEdit(note: Note, resultKey: String) {
        router.executeCommand(
            AddScreenForward {
                NoteEditFragment.newInstance(note, resultKey)
            }
        )
    }

    override fun launchAllNotes(selectedNote: Note?) {
        router.executeCommand(
            AddScreenForward {
                AllNotesFragment.newInstance(selectedNote)
            }
        )
    }
}
