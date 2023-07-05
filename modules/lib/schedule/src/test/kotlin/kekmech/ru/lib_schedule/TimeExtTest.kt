package kekmech.ru.lib_schedule

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kekmech.ru.lib_schedule.utils.atStartOfWeek
import java.time.LocalDate
import java.time.Month

class TimeExtTest : StringSpec({

    // TimeExt.atStartOfWeek() tests
    "Returns monday on MONDAY" {
        MONDAY shouldBe MONDAY.atStartOfWeek()
    }
    "Returns monday on TUESDAY" {
        MONDAY shouldBe TUESDAY.atStartOfWeek()
    }
    "Returns monday on WEDNESDAY" {
        MONDAY shouldBe WEDNESDAY.atStartOfWeek()
    }
    "Returns monday on THURSDAY" {
        MONDAY shouldBe THURSDAY.atStartOfWeek()
    }
    "Returns monday on FRIDAY" {
        MONDAY shouldBe FRIDAY.atStartOfWeek()
    }
    "Returns monday on SATURDAY" {
        MONDAY shouldBe SATURDAY.atStartOfWeek()
    }
    "Returns monday on SUNDAY" {
        MONDAY shouldBe SUNDAY.atStartOfWeek()
    }
}) {
    private companion object {
        private val MONDAY = LocalDate.of(2021, Month.FEBRUARY, 22)
        private val TUESDAY = LocalDate.of(2021, Month.FEBRUARY, 23)
        private val WEDNESDAY = LocalDate.of(2021, Month.FEBRUARY, 24)
        private val THURSDAY = LocalDate.of(2021, Month.FEBRUARY, 25)
        private val FRIDAY = LocalDate.of(2021, Month.FEBRUARY, 26)
        private val SATURDAY = LocalDate.of(2021, Month.FEBRUARY, 27)
        private val SUNDAY = LocalDate.of(2021, Month.FEBRUARY, 28)
    }
}
