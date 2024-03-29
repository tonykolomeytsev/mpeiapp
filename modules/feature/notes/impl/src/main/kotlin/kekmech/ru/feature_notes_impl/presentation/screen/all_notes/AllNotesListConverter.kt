package kekmech.ru.feature_notes_impl.presentation.screen.all_notes

import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.R
import kekmech.ru.feature_notes_impl.presentation.screen.all_notes.elm.AllNotesState
import java.time.temporal.ChronoUnit
import kekmech.ru.res_strings.R.string as Strings

private const val SHIMMER_ITEM_COUNT = 3

internal class AllNotesListConverter {

    fun map(state: AllNotesState): List<Any> {

        return when {
            state.notes == null -> List(SHIMMER_ITEM_COUNT) { ShimmerItem(R.layout.item_note_shimmer) }
            state.notes.isEmpty() -> listOf(
                EmptyStateItem(
                    titleRes = Strings.all_notes_empty_state_title,
                    subtitleRes = Strings.all_notes_empty_state_subtitle
                )
            )
            else -> mutableListOf<Any>().apply {
                getForthcomingNotes(state.notes)?.let {
                    add(SectionHeaderItem(
                        titleRes = Strings.all_notes_section_header_forthcoming
                    ))
                    add(SpaceItem.VERTICAL_12)
                    addAll(it)
                    add(SpaceItem.VERTICAL_24)
                }
                getPastNotes(state.notes)?.let {
                    add(SectionHeaderItem(
                        titleRes = Strings.all_notes_section_header_past
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
