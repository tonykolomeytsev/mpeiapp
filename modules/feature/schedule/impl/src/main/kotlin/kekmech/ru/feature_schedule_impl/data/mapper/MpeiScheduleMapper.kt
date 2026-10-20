package kekmech.ru.feature_schedule_impl.data.mapper

import kekmech.ru.feature_schedule_impl.data.model.ClassesDto
import kekmech.ru.feature_schedule_impl.data.model.ClassesTypeDto
import kekmech.ru.feature_schedule_impl.data.model.DayDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto
import kekmech.ru.feature_schedule_impl.data.model.SearchResultDto
import kekmech.ru.feature_schedule_impl.data.model.TimeDto
import kekmech.ru.feature_schedule_impl.data.model.WeekDto
import kekmech.ru.feature_schedule_impl.data.network.mpei.MpeiClassesDto
import kekmech.ru.feature_schedule_impl.data.network.mpei.MpeiSearchResultDto
import kekmech.ru.lib_schedule.utils.atStartOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

internal object MpeiScheduleMapper {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    fun mapSearchResult(mpeiResult: MpeiSearchResultDto): SearchResultDto {
        val scheduleType = if (mpeiResult.type.equals("person", ignoreCase = true)) {
            ScheduleTypeDto.PERSON
        } else {
            ScheduleTypeDto.GROUP
        }
        return SearchResultDto(
            id = mpeiResult.id.toString(),
            name = mpeiResult.label,
            description = mpeiResult.description.orEmpty(),
            type = scheduleType,
        )
    }

    fun mapToScheduleDto(
        name: String,
        id: Long,
        type: ScheduleTypeDto,
        monday: LocalDate,
        classesList: List<MpeiClassesDto>,
    ): ScheduleDto {
        val weekOfYear = monday.get(WeekFields.of(Locale.getDefault()).weekOfYear())
        val weekOfSemester = calculateWeekOfSemester(monday)

        // Group classes by date
        val classesByDate = mutableMapOf<LocalDate, MutableList<ClassesDto>>()
        for (cls in classesList) {
            val date = runCatching { LocalDate.parse(cls.date, dateFormatter) }.getOrNull() ?: continue
            val time = parseTime(cls.beginLesson, cls.endLesson)
            val lessonNumber = cls.lessonNumberStart ?: getLessonNumber(time.start)
            val person = if (cls.lecturer?.contains("вакансия", ignoreCase = true) == true) {
                ""
            } else {
                cls.lecturer?.trim().orEmpty()
            }
            val groups = cls.stream ?: cls.group ?: cls.subGroup.orEmpty()

            val classesDto = ClassesDto(
                name = cls.discipline,
                type = mapClassesType(cls.kindOfWork.orEmpty()),
                rawType = cls.kindOfWork,
                place = cls.auditorium.orEmpty(),
                groups = groups,
                person = person,
                time = time,
                number = lessonNumber,
            )

            classesByDate.getOrPut(date) { mutableListOf() }.add(classesDto)
        }

        // Create DayDto for all 7 days of the week (or sorted existing days)
        val days = (0..6).map { dayOffset ->
            val date = monday.plusDays(dayOffset.toLong())
            val classesForDay = classesByDate[date].orEmpty().sortedBy { it.time.start }
            DayDto(
                dayOfWeek = date.dayOfWeek.value,
                date = date,
                classes = classesForDay,
            )
        }

        return ScheduleDto(
            name = name,
            id = id.toString(),
            type = type,
            weeks = listOf(
                WeekDto(
                    weekOfYear = weekOfYear,
                    weekOfSemester = weekOfSemester,
                    firstDayOfWeek = monday,
                    days = days,
                )
            ),
        )
    }

    private fun mapClassesType(rawType: String): ClassesTypeDto {
        val lower = rawType.lowercase()
        return when {
            lower.contains("лек") -> ClassesTypeDto.LECTURE
            lower.contains("лаб") -> ClassesTypeDto.LAB
            lower.contains("прак") -> ClassesTypeDto.PRACTICE
            lower.contains("курс") || lower.contains("кп") -> ClassesTypeDto.COURSE
            lower.contains("экз") -> ClassesTypeDto.EXAM
            lower.contains("конс") -> ClassesTypeDto.CONSULTATION
            else -> ClassesTypeDto.UNDEFINED
        }
    }

    private fun parseTime(beginStr: String, endStr: String): TimeDto {
        val start = runCatching { LocalTime.parse(beginStr) }.getOrElse { LocalTime.of(9, 20) }
        val end = runCatching { LocalTime.parse(endStr) }.getOrElse { LocalTime.of(10, 55) }
        return TimeDto(start = start, end = end)
    }

    private fun getLessonNumber(start: LocalTime): Int {
        val totalMinutes = start.hour * 60 + start.minute
        return when {
            totalMinutes in (8 * 60 + 30)..(10 * 60 + 15) -> 1
            totalMinutes in (10 * 60 + 16)..(12 * 60 + 45) -> 2
            totalMinutes in (12 * 60 + 46)..(14 * 60 + 40) -> 3
            totalMinutes in (14 * 60 + 41)..(16 * 60 + 30) -> 4
            totalMinutes in (16 * 60 + 31)..(18 * 60 + 15) -> 5
            totalMinutes in (18 * 60 + 16)..(19 * 60 + 45) -> 6
            totalMinutes in (19 * 60 + 46)..(21 * 60 + 30) -> 7
            else -> -1
        }
    }

    private fun calculateWeekOfSemester(monday: LocalDate): Int {
        val month = monday.monthValue
        val (startMonday, startWeekNum) = if (month in 9..12 || month == 1) {
            // Fall semester
            val year = if (month == 1) monday.year - 1 else monday.year
            val sept1 = LocalDate.of(year, 9, 1)
            val firstMonday = if (sept1.dayOfWeek == DayOfWeek.SUNDAY) sept1.plusDays(1) else sept1.atStartOfWeek()
            firstMonday to 1
        } else {
            // Spring semester
            val feb1 = LocalDate.of(monday.year, 2, 1)
            val firstMonday = feb1.atStartOfWeek()
            firstMonday to 1
        }
        val diffWeeks = ChronoUnit.WEEKS.between(startMonday, monday).toInt()
        val weekNum = startWeekNum + diffWeeks
        return if (weekNum in 1..18) weekNum else -1
    }
}
