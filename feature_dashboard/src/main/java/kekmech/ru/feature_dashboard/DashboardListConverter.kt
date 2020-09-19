package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.AddActionItem
import kekmech.ru.coreui.items.NoteItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_dashboard.items.BannerLunchItem
import kekmech.ru.feature_dashboard.items.SearchFieldItem
import kekmech.ru.feature_dashboard.presentation.DashboardState
import java.time.LocalDate

class DashboardListConverter(
    context: Context
) {

    private val eventsHeader by fastLazy { SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_events),
        itemId = DashboardFragment.SECTION_HEADER_EVENTS
    ) }

    private val notesHeader by fastLazy { SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_notes),
        itemId = DashboardFragment.SECTION_HEADER_NOTES
    ) }

    fun map(state: DashboardState): List<Any> {

        return mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_24)
            add(SearchFieldItem)
            add(SpaceItem.VERTICAL_24)
            add(BannerLunchItem)
            add(SpaceItem.VERTICAL_24)
            add(eventsHeader)
            add(SpaceItem.VERTICAL_12)
            addAll(state.todayClasses)
            add(SpaceItem.VERTICAL_24)
            add(notesHeader)
            add(SpaceItem.VERTICAL_12)
            addAll(listOf(
                NoteItem("Сдать типовой расчет №1", LocalDate.now().plusDays(1), "Вычислительная ммеханика"),
                NoteItem("Лабораторная работа №1, принести бланки", LocalDate.now().plusDays(2), "Вычислительная ммеханика"),
                NoteItem("Сдать типовой расчет №2", LocalDate.now().plusDays(3), "Теория колебаний"),
                NoteItem("Подготовиться к контрольной", LocalDate.now().plusDays(4), "Физика")
            ))
            add(SpaceItem.VERTICAL_24)
            add(AddActionItem("СОЗДАТЬ ЗАМЕТКУ"))
            add(SpaceItem.VERTICAL_24)
        }
    }
}