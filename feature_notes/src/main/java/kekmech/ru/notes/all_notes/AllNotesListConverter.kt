package kekmech.ru.notes.all_notes

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.all_notes.mvi.AllNotesState
import java.time.temporal.ChronoUnit

internal class AllNotesListConverter {

    fun map(state: AllNotesState): List<Any> {

        return when {
            state.notes == null -> List(3) { ShimmerItem(0) }
            state.notes.isEmpty() -> listOf(EmptyStateItem(
                titleRes = R.string.all_notes_empty_state_title,
                subtitleRes = R.string.all_notes_empty_state_subtitle
            ))
            else -> mutableListOf<Any>().apply {
                getForthcomingNotes(state.notes)?.let {
                    add(SectionHeaderItem(
                        titleRes = R.string.all_notes_section_header_forthcoming
                    ))
                    add(SpaceItem.VERTICAL_12)
                    addAll(it)
                    add(SpaceItem.VERTICAL_24)
                }
                getPastNotes(state.notes)?.let {
                    add(SectionHeaderItem(
                        titleRes = R.string.all_notes_section_header_past
                    ))
                    add(SpaceItem.VERTICAL_12)
                    addAll(it)
                    add(SpaceItem.VERTICAL_24)
                }
            }
        }
    }

    private fun getForthcomingNotes(notes: List<Note>): List<Any>? {
        return notes
            .filter { ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) >= 0 }
            .sortedBy { it.dateTime }
            .takeIf { it.isNotEmpty() }
    }

    private fun getPastNotes(notes: List<Note>): List<Any>? {
        return notes
            .filter { ChronoUnit.DAYS.between(moscowLocalDate(), it.dateTime.toLocalDate()) < 0 }
            .sortedByDescending { it.dateTime }
            .takeIf { it.isNotEmpty() }
    }
}