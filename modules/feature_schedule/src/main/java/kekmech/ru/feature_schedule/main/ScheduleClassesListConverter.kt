package kekmech.ru.feature_schedule.main

import kekmech.ru.common_kotlin.addIf
import kekmech.ru.common_schedule.items.LunchItem
import kekmech.ru.common_schedule.items.SelfStudyItem
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.NotePreview
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.item.*
import kekmech.ru.feature_schedule.main.elm.ScheduleState

private const val CLASSES_BEFORE_LUNCH_NUMBER = 2
private const val CLASSES_AFTER_LUNCH_NUMBER = 3
private const val DAY_ITEMS_COUNT = 6

internal object ScheduleClassesListConverter {

    fun map(state: ScheduleState): List<Any> {
        val selectedSchedule = state.selectedSchedule
        val selectedScheduleWeek = selectedSchedule?.weeks?.firstOrNull()
        val shouldShowEmptyState = state.isAfterError && selectedScheduleWeek == null
        return when {
            shouldShowEmptyState -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(SpaceItem.VERTICAL_24, getEmptyStateItem())
                )
            }
            selectedSchedule == null || selectedScheduleWeek == null -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(ShimmerItem(0))
                )
            }
            else -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                val rawClasses = selectedScheduleWeek.days
                    .find { day -> day.dayOfWeek == dayOfWeek }
                    ?.classes
                    ?.onEach { classes -> classes.scheduleType = selectedSchedule.type }
                    ?: emptyList()
                val modifiedClasses = if (rawClasses.isEmpty()) {
                    listOf(SelfStudyItem)
                } else {
                    rawClasses
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
            classes.attachedNotePreview?.let { notePreviewContent ->
                add(NotePreview(notePreviewContent, linkedClasses = e))
            }
        }
    }

    private fun List<Any>.withLunch(): List<Any> = mutableListOf<Any>().apply {
        val raw = this@withLunch
        val hasSecondAndThirdClasses =
            raw.any { it is Classes && it.number == CLASSES_BEFORE_LUNCH_NUMBER } &&
                    raw.any { it is Classes && it.number == CLASSES_AFTER_LUNCH_NUMBER }
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

    private fun getEmptyStateItem() = EmptyStateItem(
        titleRes = R.string.schedule_empty_state_title,
        subtitleRes = R.string.schedule_empty_state_subtitle
    )
}