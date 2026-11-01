package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_notes_api.domain.model.Note
import java.time.temporal.ChronoUnit
import kekmech.ru.res_strings.R.string as Strings

@Immutable
internal sealed interface AllNotesListItem {
    val key: String

    data class Header(@StringRes val titleRes: Int) : AllNotesListItem {
        override val key: String get() = "header_$titleRes"
    }

    data class NoteEntry(val note: Note, override val key: String) : AllNotesListItem
}

internal class AllNotesListConverter {

    fun map(notes: List<Note>, keyOf: (Note) -> String): List<AllNotesListItem> =
        buildList {
            getForthcomingNotes(notes)?.let {
                add(AllNotesListItem.Header(Strings.all_notes_section_header_forthcoming))
                it.forEach { note -> add(AllNotesListItem.NoteEntry(note, keyOf(note))) }
            }
            getPastNotes(notes)?.let {
                add(AllNotesListItem.Header(Strings.all_notes_section_header_past))
                it.forEach { note -> add(AllNotesListItem.NoteEntry(note, keyOf(note))) }
            }
        }

    private fun getForthcomingNotes(notes: List<Note>): List<Note>? {
        return notes
            .filter { ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) >= 0 }
            .sortedBy { it.dateTime }
            .takeIf { it.isNotEmpty() }
    }

    private fun getPastNotes(notes: List<Note>): List<Note>? {
        return notes
            .filter { ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) < 0 }
            .sortedByDescending { it.dateTime }
            .takeIf { it.isNotEmpty() }
    }
}
