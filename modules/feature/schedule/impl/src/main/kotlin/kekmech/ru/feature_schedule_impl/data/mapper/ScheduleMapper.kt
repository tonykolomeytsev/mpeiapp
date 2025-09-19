package kekmech.ru.feature_schedule_impl.data.mapper

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.data.model.ClassesTypeDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto

internal object ScheduleMapper {

    private const val MaxWeekNumber = 17
    private const val NonStudyingWeekNumber = -1

    fun dtoToDomain(dto: ScheduleDto): Schedule =
        Schedule(
            name = dto.name,
            id = dto.id,
            type = when (dto.type) {
                ScheduleTypeDto.GROUP -> ScheduleType.GROUP
                ScheduleTypeDto.PERSON -> ScheduleType.PERSON
            },
            weeks = dto.weeks.map { week ->
                Week(
                    weekOfYear = week.weekOfYear,
                    weekOfSemester = when (val num = week.weekOfSemester) {
                        in 0..MaxWeekNumber -> WeekOfSemester.Studying(num)
                        else -> WeekOfSemester.NonStudying
                    },
                    firstDayOfWeek = week.firstDayOfWeek,
                    days = week.days.map { day ->
                        Day(
                            dayOfWeek = day.dayOfWeek,
                            date = day.date,
                            classes = day.classes.map { cls ->
                                Classes(
                                    name = cls.name,
                                    type = when (cls.type) {
                                        ClassesTypeDto.LAB -> ClassesType.LAB
                                        ClassesTypeDto.EXAM -> ClassesType.EXAM
                                        ClassesTypeDto.COURSE -> ClassesType.COURSE
                                        ClassesTypeDto.LECTURE -> ClassesType.LECTURE
                                        ClassesTypeDto.PRACTICE -> ClassesType.PRACTICE
                                        ClassesTypeDto.CONSULTATION -> ClassesType.CONSULTATION
                                        ClassesTypeDto.UNDEFINED -> ClassesType.UNDEFINED
                                    },
                                    rawType = cls.rawType,
                                    place = cls.place,
                                    groups = cls.groups,
                                    person = cls.person,
                                    time = Time(
                                        start = cls.time.start,
                                        end = cls.time.end,
                                    ),
                                    number = cls.number,
                                )
                            }
                        )
                    }

                )
            }
        )
}