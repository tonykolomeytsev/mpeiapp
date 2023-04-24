package kekmech.ru.mock_server.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ClassesType
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.ScheduleType
import kekmech.ru.domain_schedule.dto.Time
import kekmech.ru.domain_schedule.dto.Week
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.WeekFields
import java.util.Locale

private const val ServiceName = "schedule"

internal fun Routing.getScheduleV1() {
    get("/$ServiceName/v1/{type}/{name}/schedule/{weekOffset}") {
        when (val weekOffset = call.parameters["weekOffset"]?.toIntOrNull()) {
            null -> call.respond(HttpStatusCode.BadRequest)
            else -> call.respond(createSchedule(weekOffset))
        }
    }
}

private fun LocalDate.weekOfYear(): Int =
    get(WeekFields.of(Locale.ENGLISH).weekOfWeekBasedYear())

private fun LocalDate.monday(): LocalDate =
    minusDays(dayOfWeek.value - 1L)

@Suppress("MagicNumber")
private fun createSchedule(
    weekOffset: Int,
): Schedule {
    val weekOfYear = moscowLocalDate().weekOfYear() + weekOffset
    val weekOfSemester = 12 + weekOffset
    val monday = LocalDate.now().monday().plusWeeks(weekOffset.toLong())
    return Schedule(
        name = "Сэ-12-21",
        id = "12345",
        type = ScheduleType.GROUP,
        weeks = listOf(
            Week(
                weekOfYear = weekOfYear,
                weekOfSemester = weekOfSemester,
                firstDayOfWeek = monday,
                days = listOf(
                    Day(
                        dayOfWeek = monday.dayOfWeek.value,
                        date = monday,
                        classes = listOf(
                            Classes(
                                name = "Электротехника и электроника",
                                type = ClassesType.PRACTICE,
                                place = "Б-409",
                                groups = "Сэ-12-21",
                                person = "ст. преп. Куликова Е.А.",
                                time = Time(
                                    start = LocalTime.of(11, 10),
                                    end = LocalTime.of(12, 45),
                                ),
                                number = 2,
                            ),
                            Classes(
                                name = "Физика",
                                type = ClassesType.LECTURE,
                                place = "Н-201",
                                groups = "Сэ-12-21",
                                person = "доц. Губкин М.К.",
                                time = Time(
                                    start = LocalTime.of(13, 45),
                                    end = LocalTime.of(15, 20),
                                ),
                                number = 3,
                            ),
                            Classes(
                                name = "Электротехника и электроника",
                                type = ClassesType.LAB,
                                place = "В-400а",
                                groups = "Сэ-12-21",
                                person = "ст. преп. Куликова Е.А.",
                                time = Time(
                                    start = LocalTime.of(15, 35),
                                    end = LocalTime.of(17, 10),
                                ),
                                number = 4,
                            ),
                        ),
                    ),
                    Day(
                        dayOfWeek = monday.plusDays(1).dayOfWeek.value,
                        date = monday.plusDays(1),
                        classes = listOf(
                            Classes(
                                name = "Иностранный язык",
                                type = ClassesType.PRACTICE,
                                place = "М-812",
                                groups = "Сэ-12-21",
                                person = "",
                                time = Time(
                                    start = LocalTime.of(9, 20),
                                    end = LocalTime.of(10, 55),
                                ),
                                number = 1,
                            ),
                            Classes(
                                name = "Механика материалов и конструкций",
                                type = ClassesType.LECTURE,
                                place = "Б-413",
                                groups = "Сэ-12-21",
                                person = "доц. Догадина Т.Н.",
                                time = Time(
                                    start = LocalTime.of(11, 10),
                                    end = LocalTime.of(12, 45),
                                ),
                                number = 2,
                            ),
                            Classes(
                                name = "Элективные курсы по физической культуре и спорту",
                                type = ClassesType.PRACTICE,
                                place = "Спортзал",
                                groups = "Сэ-12-21",
                                time = Time(
                                    start = LocalTime.of(13, 45),
                                    end = LocalTime.of(15, 20),
                                ),
                                number = 3,
                            ),
                        ),
                    ),
                    Day(
                        dayOfWeek = monday.plusDays(2).dayOfWeek.value,
                        date = monday.plusDays(2),
                        classes = listOf(
                            Classes(
                                name = "Механика жидкости и газа",
                                type = ClassesType.PRACTICE,
                                place = "Б-409",
                                groups = "Сэ-12-21",
                                person = "доц. Почернина Н.И.",
                                time = Time(
                                    start = LocalTime.of(9, 20),
                                    end = LocalTime.of(10, 55),
                                ),
                                number = 1,
                            ),
                            Classes(
                                name = "Высшая математика",
                                type = ClassesType.LECTURE,
                                place = "Б-411",
                                groups = "Сэ-12-21",
                                person = "ст. преп. Капицына Т.В.",
                                time = Time(
                                    start = LocalTime.of(11, 10),
                                    end = LocalTime.of(12, 45),
                                ),
                                number = 2,
                            ),
                            Classes(
                                name = "Основы PR-коммуникаций",
                                type = ClassesType.LECTURE,
                                place = "Б-413",
                                groups = "Сэ-12-21",
                                person = "доц. Веселов А.А.",
                                time = Time(
                                    start = LocalTime.of(13, 45),
                                    end = LocalTime.of(15, 20),
                                ),
                                number = 3,
                            ),
                            Classes(
                                name = "Иностранный язык",
                                type = ClassesType.PRACTICE,
                                place = "М-910",
                                groups = "Сэ-12-21",
                                time = Time(
                                    start = LocalTime.of(15, 35),
                                    end = LocalTime.of(17, 10),
                                ),
                                number = 4,
                            ),
                        ),
                    ),
                ),
            )
        )
    )
}
