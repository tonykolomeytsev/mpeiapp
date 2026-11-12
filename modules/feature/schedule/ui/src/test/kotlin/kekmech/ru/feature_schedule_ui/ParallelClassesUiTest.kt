package kekmech.ru.feature_schedule_ui

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.compose.ClassesMinimalMetaUi
import kekmech.ru.feature_schedule_ui.compose.ParallelClassesUi
import kekmech.ru.feature_schedule_ui.compose.groupParallelClasses
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import java.time.LocalTime

class ParallelClassesUiTest : BehaviorSpec({
    Given("Parallel classes mapping") {
        When("Single classes") {
            val model = listOf(LAB_1).toUiModel(ScheduleType.GROUP)
            Then("Check Single with subgroup and teacher") {
                model.shouldBeInstanceOf<ParallelClassesUi.Single>()
                model.group shouldBe "A-08-19\\1п"
                model.teacher shouldBe "Ivanov I.I."
            }
        }
        When("Duplicated classes") {
            val model = listOf(LAB_1, LAB_1.copy()).toUiModel(ScheduleType.GROUP)
            Then("Check duplicates are merged into Single") {
                model.shouldBeInstanceOf<ParallelClassesUi.Single>()
            }
        }
        When("Same name and type") {
            val model = listOf(LAB_1, LAB_2).toUiModel(ScheduleType.GROUP)
            Then("Check SameNameAndType with every subgroup") {
                model.shouldBeInstanceOf<ParallelClassesUi.SameNameAndType>()
                model.differentMetaInfo shouldBe listOf(
                    ClassesMinimalMetaUi(place = "E-306", group = "A-08-19\\1п", teacher = "Ivanov I.I."),
                    ClassesMinimalMetaUi(place = "E-308", group = "A-08-19\\2п", teacher = "Petrov P.P."),
                )
            }
        }
        When("Same name, different types") {
            val model = listOf(LAB_1, LAB_2.copy(type = ClassesType.PRACTICE)).toUiModel(ScheduleType.GROUP)
            Then("Check SameNameDifferentTypes") {
                model.shouldBeInstanceOf<ParallelClassesUi.SameNameDifferentTypes>()
                model.differentMetaInfo.map { it.type } shouldBe listOf(ClassesType.LAB, ClassesType.PRACTICE)
            }
        }
        When("Same type, different raw types") {
            val model = listOf(
                LAB_1.copy(type = ClassesType.UNDEFINED, rawType = "Зачёт"),
                LAB_2.copy(type = ClassesType.UNDEFINED, rawType = "Экзамен"),
            ).toUiModel(ScheduleType.GROUP)
            Then("Check SameNameDifferentTypes") {
                model.shouldBeInstanceOf<ParallelClassesUi.SameNameDifferentTypes>()
            }
        }
        When("Different names") {
            val classes = listOf(LAB_1, LAB_2.copy(name = "Foreign language"))
            Then("Check they can't be shown as a single card") {
                shouldThrow<IllegalArgumentException> { classes.toUiModel(ScheduleType.GROUP) }
            }
        }
        When("Person schedule") {
            val model = listOf(LAB_1).toUiModel(ScheduleType.PERSON)
            Then("Check groups are shown instead of the teacher") {
                model.shouldBeInstanceOf<ParallelClassesUi.Single>()
                model.group shouldBe "A-08-19\\1п"
                model.teacher shouldBe null
            }
        }
    }
    Given("Parallel classes grouping") {
        When("Parallel classes between regular ones") {
            val first = LAB_1.copy(name = "First", number = 1, time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)))
            val last = LAB_1.copy(name = "Last", number = 4, time = Time(LocalTime.of(15, 35), LocalTime.of(17, 10)))
            val grouped = listOf(first, LAB_1, LAB_2, last).groupParallelClasses()
            Then("Check only classes with the same number and time are grouped") {
                grouped shouldBe listOf(listOf(first), listOf(LAB_1, LAB_2), listOf(last))
            }
        }
        When("Parallel classes with different names") {
            val other = LAB_2.copy(name = "Foreign language")
            val grouped = listOf(LAB_1, other, LAB_2).groupParallelClasses()
            Then("Check classes with different names are not grouped") {
                grouped shouldBe listOf(listOf(LAB_1, LAB_2), listOf(other))
            }
        }
    }
}) {

    private companion object {

        private val LAB_1 = Classes(
            name = "Materials",
            type = ClassesType.LAB,
            rawType = null,
            place = "E-306",
            groups = "A-08-19\\1п",
            person = "Ivanov I.I.",
            time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20)),
            number = 3,
        )
        private val LAB_2 = LAB_1.copy(
            place = "E-308",
            groups = "A-08-19\\2п",
            person = "Petrov P.P.",
        )
    }
}
