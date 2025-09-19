package kekmech.ru.mock_server.routing.schedule

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.mock_server.randomResponseDelay
import kekmech.ru.mock_server.routing.schedule.model.ClassesDto
import kekmech.ru.mock_server.routing.schedule.model.ClassesTypeDto
import kekmech.ru.mock_server.routing.schedule.model.DayDto
import kekmech.ru.mock_server.routing.schedule.model.ScheduleDto
import kekmech.ru.mock_server.routing.schedule.model.ScheduleTypeDto
import kekmech.ru.mock_server.routing.schedule.model.TimeDto
import kekmech.ru.mock_server.routing.schedule.model.WeekDto
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
): ScheduleDto {
    val weekOfYear = moscowLocalDate().weekOfYear() + weekOffset
    val weekOfSemester = 12 + weekOffset
    val monday = LocalDate.now().monday().plusWeeks(weekOffset.toLong())
    return ScheduleDto(
        name = "Сэ-12-21",
        id = "12345",
        type = ScheduleTypeDto.GROUP,
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
): WeekDto {
    val tuesday = monday.plusDays(1)
    val wednesday = monday.plusDays(2)
    val thursday = monday.plusDays(3)
    val friday = monday.plusDays(4)
    return WeekDto(
        weekOfYear = weekOfYear,
        weekOfSemester = weekOfSemester,
        firstDayOfWeek = monday,
        days = listOf(
            DayDto(
                dayOfWeek = monday.dayOfWeek.value,
                date = monday,
                classes = listOf(
                    ClassesDto(
                        name = "Электротехника и электроника",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Физика",
                        type = ClassesTypeDto.LECTURE,
                        place = "Н-201",
                        groups = "Сэ-12-21",
                        person = "доц. Губкин М.К.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Электротехника и электроника",
                        type = ClassesTypeDto.LAB,
                        place = "В-400а",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Лабораторная",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = tuesday.dayOfWeek.value,
                date = tuesday,
                classes = listOf(
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-812",
                        groups = "Сэ-12-21",
                        person = "",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Механика материалов и конструкций",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Элективные курсы по физической культуре и спорту",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = wednesday.dayOfWeek.value,
                date = wednesday,
                classes = listOf(
                    ClassesDto(
                        name = "Механика жидкости и газа",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "доц. Почернина Н.И.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Высшая математика",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-411",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Основы PR-коммуникаций",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "доц. Веселов А.А.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-910",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = thursday.dayOfWeek.value,
                date = thursday,
                classes = listOf(
                    ClassesDto(
                        name = "Высшая математика",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Механика материалов и конструкций",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Высшая математика",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Элективные курсы по физической культуре и спорту",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = friday.dayOfWeek.value,
                date = friday,
                classes = listOf(
                    ClassesDto(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-406",
                        groups = "Сэ-12-21",
                        person = "ассист. Салимов М.С.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Механика жидкости и газа",
                        type = ClassesTypeDto.LECTURE,
                        place = "Г-404",
                        groups = "Сэ-12-21",
                        person = "проф. Ляпин В.Ю.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Электротехника и электроника",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-814",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
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
): WeekDto {
    val tuesday = monday.plusDays(1)
    val wednesday = monday.plusDays(2)
    val thursday = monday.plusDays(3)
    val friday = monday.plusDays(4)
    return WeekDto(
        weekOfYear = weekOfYear,
        weekOfSemester = weekOfSemester,
        firstDayOfWeek = monday,
        days = listOf(
            DayDto(
                dayOfWeek = tuesday.dayOfWeek.value,
                date = tuesday,
                classes = listOf(
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-812",
                        groups = "Сэ-12-21",
                        person = "",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Механика материалов и конструкций",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Физическая культура и спорт",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = wednesday.dayOfWeek.value,
                date = wednesday,
                classes = listOf(
                    ClassesDto(
                        name = "Физика",
                        type = ClassesTypeDto.LAB,
                        place = "А-305",
                        groups = "Сэ-12-21",
                        person = "доц. Губкин М.К.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лабораторная",
                    ),
                    ClassesDto(
                        name = "Физика",
                        type = ClassesTypeDto.LAB,
                        place = "А-305",
                        groups = "Сэ-12-21",
                        person = "проф. Иванов Д.А.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Лабораторная",
                    ),
                    ClassesDto(
                        name = "Основы PR-коммуникаций",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "доц. Веселов А.А.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-910",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = thursday.dayOfWeek.value,
                date = thursday,
                classes = listOf(
                    ClassesDto(
                        name = "Высшая математика",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Механика материалов и конструкций",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "доц. Догадина Т.Н.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Высшая математика",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Б-413",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Капицына Т.В.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Практика",
                    ),
                    ClassesDto(
                        name = "Элективные курсы по физической культуре и спорту",
                        type = ClassesTypeDto.PRACTICE,
                        place = "Спортзал",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            ),
            DayDto(
                dayOfWeek = friday.dayOfWeek.value,
                date = friday,
                classes = listOf(
                    ClassesDto(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesTypeDto.LAB,
                        place = "Б-406",
                        groups = """Сэ-12-21\1п.""",
                        person = "ассист. Салимов М.С.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лабораторная",
                    ),
                    ClassesDto(
                        name = "Основы компьютерного моделирования и проектирования робототехнических устройств и систем",
                        type = ClassesTypeDto.LAB,
                        place = "Б-406",
                        groups = """Сэ-12-21\2п.""",
                        person = "ассист. Салимов М.С.",
                        time = TimeDto(
                            start = LocalTime.of(9, 20),
                            end = LocalTime.of(10, 55),
                        ),
                        number = 1,
                        rawType = "Лабораторная",
                    ),
                    ClassesDto(
                        name = "Механика жидкости и газа",
                        type = ClassesTypeDto.LECTURE,
                        place = "Г-404",
                        groups = "Сэ-12-21",
                        person = "проф. Ляпин В.Ю.",
                        time = TimeDto(
                            start = LocalTime.of(11, 10),
                            end = LocalTime.of(12, 45),
                        ),
                        number = 2,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Электротехника и электроника",
                        type = ClassesTypeDto.LECTURE,
                        place = "Б-409",
                        groups = "Сэ-12-21",
                        person = "ст. преп. Куликова Е.А.",
                        time = TimeDto(
                            start = LocalTime.of(13, 45),
                            end = LocalTime.of(15, 20),
                        ),
                        number = 3,
                        rawType = "Лекция",
                    ),
                    ClassesDto(
                        name = "Иностранный язык",
                        type = ClassesTypeDto.PRACTICE,
                        place = "М-814",
                        groups = "Сэ-12-21",
                        time = TimeDto(
                            start = LocalTime.of(15, 35),
                            end = LocalTime.of(17, 10),
                        ),
                        number = 4,
                        rawType = "Практика",
                        person = "",
                    ),
                ),
            )
        ),
    )
}
