package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.items.AddActionItem
import kekmech.ru.coreui.items.NoteItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ClassesStackType
import kekmech.ru.feature_dashboard.items.BannerLunchItem
import kekmech.ru.feature_dashboard.items.BannerOpenSourceItem
import kekmech.ru.feature_dashboard.items.DayStatusItem
import kekmech.ru.feature_dashboard.items.SearchFieldItem
import kekmech.ru.feature_dashboard.presentation.DashboardState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class DashboardListConverter(
    private val context: Context
) {

    private val notesHeader by fastLazy { SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_notes),
        itemId = DashboardFragment.SECTION_HEADER_NOTES
    ) }

    private val formatter = PrettyDateFormatter(context)

    fun map(state: DashboardState): List<Any> {

        return mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_12)
            createDayStatusItem(state)?.let {
                add(it)
                add(SpaceItem.VERTICAL_24)
            }
            add(SearchFieldItem)
            add(SpaceItem.VERTICAL_24)
            add(BannerLunchItem)
            add(BannerOpenSourceItem)
            createClassesEventsItems(state)?.let { (header, classes) ->
                add(SpaceItem.VERTICAL_16)
                add(header)
                add(SpaceItem.VERTICAL_12)
                addAll(classes)
            }
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

    private fun createDayStatusItem(state: DashboardState): DayStatusItem? {
        val weekStatus = state.weekOfSemester?.let { weekOfSemester ->
            if (weekOfSemester in 1..16) {
                context.getString(R.string.dashboard_day_status_semester, weekOfSemester)
            } else {
                context.getString(R.string.dashboard_day_status_not_semester)
            }
        }
        return DayStatusItem(
            formatter.formatAbsolute(LocalDate.now()),
            weekStatus.orEmpty()
        )
    }

    private fun createClassesEventsItems(state: DashboardState): Pair<SectionHeaderItem, List<Any>>? {
        val nowDate = LocalDate.now()
        val nowTime = LocalTime.now()

        val isSunday = nowDate.dayOfWeek == DayOfWeek.SUNDAY
        val isEvening = state.todayClasses?.lastOrNull()?.time?.end?.let { it < nowTime } ?: false
        val hasNoClassesToday = state.todayClasses.isNullOrEmpty()

        when {
            isSunday || isEvening || hasNoClassesToday -> {
                val headerItem = createHeaderItem(context.getString(R.string.dashboard_events_tomorrow))
                val tomorrowClasses = state.tomorrowClasses
                    ?.paintClasses() ?: return null // если на завтра пар тоже нет, то не возвращаем вообще ничего
                return headerItem to tomorrowClasses
            }
            else -> {
                val headerItem = createHeaderItem(context.getString(R.string.dashboard_events_today))
                val nextTodayClasses = state.todayClasses
                    ?.filter { it.time.end > nowTime } // не берем прошедшие пары
                    ?.paintClasses() ?: return null // если на сегодня пар нет, то не возвращаем ничего
                return headerItem to nextTodayClasses
            }
        }
    }

    /**
     *  Все кроме первой пары в списке будут с меньшим количеством подробностей
     */
    private fun List<Classes>.paintClasses() = mapIndexed { index, e ->
        e.stackType = if (index == 0) null else ClassesStackType.MIDDLE; e
    }

    private fun createHeaderItem(subtitle: String) = SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_events),
        subtitle = subtitle,
        itemId = DashboardFragment.SECTION_HEADER_EVENTS
    )
}