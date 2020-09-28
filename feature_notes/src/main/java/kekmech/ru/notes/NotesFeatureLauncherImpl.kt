package kekmech.ru.notes

import androidx.fragment.app.Fragment
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.notes.edit.NoteEditFragment
import kekmech.ru.notes.note_list.NoteListFragment
import java.time.LocalDate

class NotesFeatureLauncherImpl(
    private val router: Router
) : NotesFeatureLauncher {

    override fun launchNoteList(
        selectedClasses: Classes,
        selectedDate: LocalDate,
        targetFragment: Fragment?,
        requestCode: Int?
    ) {
        router.executeCommand(ShowDialog {
            NoteListFragment.newInstance(selectedClasses, selectedDate).also {
                if (targetFragment != null && requestCode != null) {
                    it.withResultFor(targetFragment, requestCode)
                }
            }
        })
    }

    override fun launchNoteEdit(
        note: Note,
        targetFragment: Fragment?,
        requestCode: Int?
    ) {
        router.executeCommand(AddScreenForward {
            NoteEditFragment.newInstance(note).also {
                if (targetFragment != null && requestCode != null) {
                    it.withResultFor(targetFragment, requestCode)
                }
            }
        })
    }
}