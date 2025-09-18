package kekmech.ru.mock_server.routing.schedule

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.mock_server.randomResponseDelay
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.WeekFields
import java.util.Locale

internal fun Routing.getScheduleV1() {
    get("/schedule/v1/{type}/{name}/schedule/{weekOffset}") {
        randomResponseDelay()
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
            if (weekOffset % 2 == 0) {
                createEvenWeek(weekOfYear, weekOfSemester, monday)
            } else {
                createOddWeek(weekOfYear, weekOfSemester, monday)
            }
        )
    )
}

private fun createEvenWeek(
    weekOfYear: Int,
    weekOfSemester: Int,
    monday: LocalDate,
): Week {
    val tuesday = monday.plusDays(1)
    val wednesday = monday.plusDays(2)
    val thursday = monday.plusDays(3)
    val friday = monday.plusDays(4)
    return Week(
        weekOfYear = weekOfYear,
        weekOfSemester = WeekOfSemester.Studying(weekOfSemester),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = tuesday.dayOfWeek.value,
                date = tuesday,
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = wednesday.dayOfWeek.value,
                date = wednesday,
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = thursday.dayOfWeek.value,
                date = thursday,
                classes = listOf(
                    Classes(
                        name = "Высшая математика",
                        type = ClassesType.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Механика материалов и конструкций",
                        type = ClassesType.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = Time(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Высшая математика",
                        type = ClassesType.PRACTICE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = Time(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Элективные курсы по физической культуре и спорту",
                        type = ClassesType.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = Time(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = friday.dayOfWeek.value,
                date = friday,
                classes = listOf(
                    Classes(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesType.LECTURE,
                        place = "Б-406",
                        groups = "Сэ-12-21",
                        person = "ассист. Салимов М.С.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Механика жидкости и газа",
                        type = ClassesType.LECTURE,
                        place = "Г-404",
                        groups = "Сэ-12-21",
                        person = "проф. Ляпин В.Ю.",
                        time = Time(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Электротехника и электроника",
                        type = ClassesType.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = Time(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Иностранный язык",
                        type = ClassesType.PRACTICE,
                        place = "М-814",
                        groups = "Сэ-12-21",
                        time = Time(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
        ),
    )
}

private fun createOddWeek(
    weekOfYear: Int,
    weekOfSemester: Int,
    monday: LocalDate,
): Week {
    val tuesday = monday.plusDays(1)
    val wednesday = monday.plusDays(2)
    val thursday = monday.plusDays(3)
    val friday = monday.plusDays(4)
    return Week(
        weekOfYear = weekOfYear,
        weekOfSemester = WeekOfSemester.Studying(weekOfSemester),
        firstDayOfWeek = monday,
        days = listOf(
            Day(
                dayOfWeek = tuesday.dayOfWeek.value,
                date = tuesday,
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Физическая культура и спорт",
                        type = ClassesType.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = Time(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = wednesday.dayOfWeek.value,
                date = wednesday,
                classes = listOf(
                    Classes(
                        name = "Физика",
                        type = ClassesType.LAB,
                        place = "А-305",
                        groups = "Сэ-12-21",
                        person = "доц. Губкин М.К.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Физика",
                        type = ClassesType.LAB,
                        place = "А-305",
                        groups = "Сэ-12-21",
                        person = "проф. Иванов Д.А.",
                        time = Time(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
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
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = thursday.dayOfWeek.value,
                date = thursday,
                classes = listOf(
                    Classes(
                        name = "Высшая математика",
                        type = ClassesType.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Механика материалов и конструкций",
                        type = ClassesType.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = Time(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Высшая математика",
                        type = ClassesType.PRACTICE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = Time(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Элективные курсы по физической культуре и спорту",
                        type = ClassesType.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = Time(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            ),
            Day(
                dayOfWeek = friday.dayOfWeek.value,
                date = friday,
                classes = listOf(
                    Classes(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesType.LAB,
                        place = "Б-406",
                        groups = """Сэ-12-21\1п.""",
                        person = "ассист. Салимов М.С.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesType.LAB,
                        place = "Б-406",
                        groups = """Сэ-12-21\2п.""",
                        person = "ассист. Салимов М.С.",
                        time = Time(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Механика жидкости и газа",
                        type = ClassesType.LECTURE,
                        place = "Г-404",
                        groups = "Сэ-12-21",
                        person = "проф. Ляпин В.Ю.",
                        time = Time(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Электротехника и электроника",
                        type = ClassesType.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = Time(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                    ),
                    Classes(
                        name = "Иностранный язык",
                        type = ClassesType.PRACTICE,
                        place = "М-814",
                        groups = "Сэ-12-21",
                        time = Time(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = TODO(),
                        attachedNotePreview = TODO(),
                        progress = TODO(),
                        person = TODO(),
                    ),
                ),
            )
        ),
    )
}
