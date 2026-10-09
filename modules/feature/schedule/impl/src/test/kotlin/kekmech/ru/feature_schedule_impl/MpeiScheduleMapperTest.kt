package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_impl.data.mapper.MpeiScheduleMapper
import kekmech.ru.feature_schedule_impl.data.model.ClassesTypeDto
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto
import kekmech.ru.feature_schedule_impl.data.network.mpei.MpeiClassesDto
import kekmech.ru.feature_schedule_impl.data.network.mpei.MpeiSearchResultDto
import java.time.LocalDate

class MpeiScheduleMapperTest : BehaviorSpec({

    Given("MPEI search result DTO") {
        When("Item is a group") {
            val dto = MpeiSearchResultDto(
                id = 21072,
                label = "Эл-01-23",
                description = "Институт электроэнергетики",
                type = "group",
            )
            val result = MpeiScheduleMapper.mapSearchResult(dto)

            Then("Result correctly maps to SearchResultDto") {
                result.id shouldBe "21072"
                result.name shouldBe "Эл-01-23"
                result.description shouldBe "Институт электроэнергетики"
                result.type shouldBe ScheduleTypeDto.GROUP
            }
        }

        When("Item is a person") {
            val dto = MpeiSearchResultDto(
                id = 15581,
                label = "Сорокин С.А.",
                description = "Кафедра безопасности и информационных технологий",
                type = "person",
            )
            val result = MpeiScheduleMapper.mapSearchResult(dto)

            Then("Result correctly maps to SearchResultDto as PERSON") {
                result.id shouldBe "15581"
                result.name shouldBe "Сорокин С.А."
                result.type shouldBe ScheduleTypeDto.PERSON
            }
        }
    }

    Given("MPEI classes list") {
        val monday = LocalDate.of(2026, 10, 5)
        val mpeiClasses = listOf(
            MpeiClassesDto(
                auditorium = "М-522",
                beginLesson = "17:20",
                endLesson = "18:50",
                date = "2026.10.09",
                discipline = "Программное обеспечение автоматизированных систем",
                kindOfWork = "Лабораторная работа",
                lecturer = "доц. Сорокин С.А.",
                group = "Эл-01-23",
                subGroup = "Эл-01-23\\1п/г.",
                stream = null,
                dayOfWeek = 5,
                lessonNumberStart = 5,
            )
        )

        When("Mapping to ScheduleDto") {
            val scheduleDto = MpeiScheduleMapper.mapToScheduleDto(
                name = "Эл-01-23",
                id = 21072,
                type = ScheduleTypeDto.GROUP,
                monday = monday,
                classesList = mpeiClasses,
            )

            Then("Schedule contains one week with 7 days") {
                scheduleDto.name shouldBe "Эл-01-23"
                scheduleDto.id shouldBe "21072"
                scheduleDto.type shouldBe ScheduleTypeDto.GROUP
                scheduleDto.weeks shouldHaveSize 1

                val week = scheduleDto.weeks.first()
                week.firstDayOfWeek shouldBe monday
                week.days shouldHaveSize 7

                // Friday is day index 4 (5th day)
                val friday = week.days.first { it.dayOfWeek == 5 }
                friday.classes shouldHaveSize 1

                val cls = friday.classes.first()
                cls.name shouldBe "Программное обеспечение автоматизированных систем"
                cls.place shouldBe "М-522"
                cls.person shouldBe "доц. Сорокин С.А."
                cls.type shouldBe ClassesTypeDto.LAB
                cls.number shouldBe 5
                cls.time.start.hour shouldBe 17
                cls.time.start.minute shouldBe 20
                cls.time.end.hour shouldBe 18
                cls.time.end.minute shouldBe 50
            }
        }
    }
})
