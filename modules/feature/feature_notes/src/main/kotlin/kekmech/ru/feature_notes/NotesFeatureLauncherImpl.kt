package kekmech.ru.feature_notes

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_notes.all_notes.AllNotesFragment
import kekmech.ru.feature_notes.edit.NoteEditFragment
import kekmech.ru.feature_notes.note_list.NoteListFragment
import java.time.LocalDate

internal class NotesFeatureLauncherImpl(
    private val router: Router,
) : NotesFeatureLauncher {

    override fun launchNoteList(selectedClasses: Classes, selectedDate: LocalDate, resultKey: String) {
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
