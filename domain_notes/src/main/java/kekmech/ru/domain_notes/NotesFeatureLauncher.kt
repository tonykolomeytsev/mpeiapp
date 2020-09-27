package kekmech.ru.domain_notes

import androidx.fragment.app.Fragment
import kekmech.ru.domain_schedule.dto.Classes
import java.time.LocalDate

interface NotesFeatureLauncher {
    fun launchNoteList(
        selectedClasses: Classes,
        selectedDate: LocalDate,
        targetFragment: Fragment? = null,
        requestCode: Int? = null
    )
}