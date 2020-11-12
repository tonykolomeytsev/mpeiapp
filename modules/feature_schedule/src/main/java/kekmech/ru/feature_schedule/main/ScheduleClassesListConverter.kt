package kekmech.ru.feature_schedule.main

import kekmech.ru.common_kotlin.addIf
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.NotePreview
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.item.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleState

internal object ScheduleClassesListConverter {

    fun map(state: ScheduleState): List<Any> {
        val selectedWeekSchedule = state.schedule[state.selectedDay.weekOffset]?.weeks?.first()
        return when {
            (!state.isLoading && state.isAfterError && selectedWeekSchedule == null) -> List(6) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(SpaceItem.VERTICAL_24, getEmptyStateItem())
                )
            }
            selectedWeekSchedule == null -> List(6) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(getClassesShimmerItem())
                )
            }
            else -> List(6) {
                val dayOfWeek = it + 1
                val rawClasses = selectedWeekSchedule.days.find { day -> day.dayOfWeek == dayOfWeek }?.classes ?: emptyList()
                val modifiedClasses = if (rawClasses.isEmpty()) {
                    listOf(SelfStudyItem)
                } else {
                    rawClasses
                        .detectStackClasses()
                        .withWindows()
                        .withLunch()
                        .withNotePreview()
                }
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = modifiedClasses
                )
            }
        }
    }

    private fun List<Any>.withNotePreview(): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withNotePreview
        for (e in raw) {
            add(e)
            val classes = e as? Classes ?: continue
            val notePreviewContent = classes.attachedNotePreview ?: continue
            add(NotePreview(notePreviewContent, linkedClasses = e))
        }
    }

    private fun List<Any>.withLunch(): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withLunch
        val hasSecondAndThirdClasses = raw.any { it is Classes && it.number == 2 } && raw.any { it is Classes && it.number == 3 }
        if (hasSecondAndThirdClasses) {
            val indexOfLastSecondClasses = raw.indexOfLast { it is Classes && it.number == 2 }
            raw.forEachIndexed { index, e ->
                add(e)
                addIf(LunchItem) { index == indexOfLastSecondClasses }
            }
        } else {
            addAll(raw)
        }
    }

    private fun List<Classes>.withWindows(): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withWindows
        if (raw.size > 1) {
            add(raw.first())
            for (i in 1 until raw.size) {
                val currClasses = raw[i]
                val prevClasses = raw[i - 1]
                addIf(
                    WindowItem(prevClasses.time.end, currClasses.time.start)
                ) { currClasses.number - prevClasses.number > 1 }
                add(currClasses)
            }
        } else {
            addAll(raw)
        }
    }

    private fun List<Classes>.detectStackClasses(): List<Classes> {
        for (i in 1 until size) {
            val currItem = this[i]
            val prevItem = this[i - 1]
            if (currItem.number == prevItem.number) {
                currItem.isInStack = true
                prevItem.isInStack = true
            }
        }
        return this
    }

    private fun getEmptyStateItem() = EmptyStateItem(
        titleRes = R.string.schedule_empty_state_title,
        subtitleRes = R.string.schedule_empty_state_subtitle
    )
}