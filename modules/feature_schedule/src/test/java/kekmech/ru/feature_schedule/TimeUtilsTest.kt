package kekmech.ru.feature_schedule

import io.kotest.core.spec.style.StringSpec
import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.items.WeekItem
import kekmech.ru.common_schedule.utils.TimeUtils
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate
import java.time.Month

class TimeUtilsTest : StringSpec({

    "Generate current week items" {
        assertEquals(
            WeekItem(
                weekOffset = 0,
                firstDayOfWeek = CURRENT_MONDAY,
                dayItems = listOf(
                    DayItem(CURRENT_MONDAY, weekOffset = 0, isSelected = false),
                    DayItem(CURRENT_MONDAY.plusDays(1L), weekOffset = 0, isSelected = false),
                    DayItem(CURRENT_MONDAY.plusDays(2L), weekOffset = 0, isSelected = false),
                    DayItem(CURRENT_MONDAY.plusDays(3L), weekOffset = 0, isSelected = false),
                    DayItem(CURRENT_MONDAY.plusDays(4L), weekOffset = 0, isSelected = false),
                    DayItem(CURRENT_MONDAY.plusDays(5L), weekOffset = 0, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(0, CURRENT_MONDAY)
        )
    }

    "Generate previous week items" {
        assertEquals(
            WeekItem(
                weekOffset = -1,
                firstDayOfWeek = PREV_MONDAY,
                dayItems = listOf(
                    DayItem(PREV_MONDAY, weekOffset = -1, isSelected = false),
                    DayItem(PREV_MONDAY.plusDays(1L), weekOffset = -1, isSelected = false),
                    DayItem(PREV_MONDAY.plusDays(2L), weekOffset = -1, isSelected = false),
                    DayItem(PREV_MONDAY.plusDays(3L), weekOffset = -1, isSelected = false),
                    DayItem(PREV_MONDAY.plusDays(4L), weekOffset = -1, isSelected = false),
                    DayItem(PREV_MONDAY.plusDays(5L), weekOffset = -1, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(-1, PREV_MONDAY)
        )
    }

    "Generate 2x previous week items" {
        assertEquals(
            WeekItem(
                weekOffset = -2,
                firstDayOfWeek = PREV2_MONDAY,
                dayItems = listOf(
                    DayItem(PREV2_MONDAY, weekOffset = -2, isSelected = false),
                    DayItem(PREV2_MONDAY.plusDays(1L), weekOffset = -2, isSelected = false),
                    DayItem(PREV2_MONDAY.plusDays(2L), weekOffset = -2, isSelected = false),
                    DayItem(PREV2_MONDAY.plusDays(3L), weekOffset = -2, isSelected = false),
                    DayItem(PREV2_MONDAY.plusDays(4L), weekOffset = -2, isSelected = false),
                    DayItem(PREV2_MONDAY.plusDays(5L), weekOffset = -2, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(-2, PREV2_MONDAY)
        )
    }

    "Generate 3x previous week items" {
        val offset = -3
        assertEquals(
            WeekItem(
                weekOffset = offset,
                firstDayOfWeek = PREV3_MONDAY,
                dayItems = listOf(
                    DayItem(PREV3_MONDAY, weekOffset = offset, isSelected = false),
                    DayItem(PREV3_MONDAY.plusDays(1L), weekOffset = offset, isSelected = false),
                    DayItem(PREV3_MONDAY.plusDays(2L), weekOffset = offset, isSelected = false),
                    DayItem(PREV3_MONDAY.plusDays(3L), weekOffset = offset, isSelected = false),
                    DayItem(PREV3_MONDAY.plusDays(4L), weekOffset = offset, isSelected = false),
                    DayItem(PREV3_MONDAY.plusDays(5L), weekOffset = offset, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(offset, PREV3_MONDAY)
        )
    }

    "Generate 4x previous week items" {
        val offset = -4
        assertEquals(
            WeekItem(
                weekOffset = offset,
                firstDayOfWeek = PREV4_MONDAY,
                dayItems = listOf(
                    DayItem(PREV4_MONDAY, weekOffset = offset, isSelected = false),
                    DayItem(PREV4_MONDAY.plusDays(1L), weekOffset = offset, isSelected = false),
                    DayItem(PREV4_MONDAY.plusDays(2L), weekOffset = offset, isSelected = false),
                    DayItem(PREV4_MONDAY.plusDays(3L), weekOffset = offset, isSelected = false),
                    DayItem(PREV4_MONDAY.plusDays(4L), weekOffset = offset, isSelected = false),
                    DayItem(PREV4_MONDAY.plusDays(5L), weekOffset = offset, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(offset, PREV4_MONDAY)
        )
    }

    "Generate 5x previous week items" {
        val offset = -5
        assertEquals(
            WeekItem(
                weekOffset = offset,
                firstDayOfWeek = PREV5_MONDAY,
                dayItems = listOf(
                    DayItem(PREV5_MONDAY, weekOffset = offset, isSelected = false),
                    DayItem(PREV5_MONDAY.plusDays(1L), weekOffset = offset, isSelected = false),
                    DayItem(PREV5_MONDAY.plusDays(2L), weekOffset = offset, isSelected = false),
                    DayItem(PREV5_MONDAY.plusDays(3L), weekOffset = offset, isSelected = false),
                    DayItem(PREV5_MONDAY.plusDays(4L), weekOffset = offset, isSelected = false),
                    DayItem(PREV5_MONDAY.plusDays(5L), weekOffset = offset, isSelected = false)
                )
            ),
            TimeUtils.createWeekItem(offset, PREV5_MONDAY)
        )
    }
}) {
    companion object {
        private val CURRENT_MONDAY = LocalDate.of(2020, Month.SEPTEMBER, 14)
        private val PREV_MONDAY = CURRENT_MONDAY.minusWeeks(1)
        private val PREV2_MONDAY = CURRENT_MONDAY.minusWeeks(2)
        private val PREV3_MONDAY = CURRENT_MONDAY.minusWeeks(3)
        private val PREV4_MONDAY = CURRENT_MONDAY.minusWeeks(4)
        private val PREV5_MONDAY = CURRENT_MONDAY.minusWeeks(5)
    }
}