package kekmech.ru.notes.note_list

import android.content.Context
import kekmech.ru.coreui.items.AddActionItem
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.notes.R
import kekmech.ru.notes.note_list.mvi.NoteListState

internal class NoteListConverter(private val context: Context) {

    private val addNoteItem = AddActionItem(context.getString(R.string.note_list_section_add))

    fun map(state: NoteListState): List<Any> {

        return mutableListOf<Any>().apply {
            add(PullItem)
            add(SpaceItem.VERTICAL_8)
            if (state.notes.isEmpty()) {
                add(SectionHeaderItem(
                    titleRes = R.string.note_list_section_header,
                    subtitle = context.getString(R.string.note_list_section_subtitle_empty)
                ))
            } else {
                val size = state.notes.size
                add(SectionHeaderItem(
                    titleRes = R.string.note_list_section_header,
                    subtitle = context.resources.getQuantityString(R.plurals.note_list_section_subtitle, size, size)
                ))
                add(SpaceItem.VERTICAL_16)
                addAll(state.notes)
            }

            add(SpaceItem.VERTICAL_24)
            add(addNoteItem)
        }
    }
}