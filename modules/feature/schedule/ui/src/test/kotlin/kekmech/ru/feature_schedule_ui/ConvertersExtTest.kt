package kekmech.ru.feature_schedule_ui

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.items.LunchItem
import kekmech.ru.feature_schedule_ui.items.WindowItem
import kekmech.ru.feature_schedule_ui.utils.withLunch
import kekmech.ru.feature_schedule_ui.utils.withProgressPreview
import kekmech.ru.feature_schedule_ui.utils.withWindows
import java.time.LocalTime

class ConvertersExtTest : BehaviorSpec({
    Given("Lunch insertion") {
        When("First and second classes") {
            val givenClasses = listOf<Any>(CLASSES_1, CLASSES_2)
            Then("Check no lunch") {
                givenClasses.withLunch() shouldBe givenClasses
            }
        }
        When("Second and third classes") {
            val givenClasses = listOf<Any>(CLASSES_2, CLASSES_3)
            Then("Check has lunch") {
                givenClasses.withLunch().shouldContainExactly(CLASSES_2, LunchItem, CLASSES_3)
            }
        }
        When("Third and fourth classes") {
            val givenClasses = listOf<Any>(CLASSES_3, CLASSES_4)
            Then("Check no lunch") {
                givenClasses.withLunch() shouldBe givenClasses
            }
        }
        When("Second and fourth classes") {
            val givenClasses = listOf<Any>(CLASSES_2, CLASSES_4)
            Then("Check no lunch") {
                givenClasses.withLunch() shouldBe givenClasses
            }
        }
        When("First and third classes") {
            val givenClasses = listOf<Any>(CLASSES_1, CLASSES_3)
            Then("Check no lunch") {
                givenClasses.withLunch() shouldBe givenClasses
            }
        }
        When("First and fourth classes") {
            val givenClasses = listOf<Any>(CLASSES_1, CLASSES_4)
            Then("Check no lunch") {
                givenClasses.withLunch() shouldBe givenClasses
            }
        }
    }
    Given("Windows insertion") {
        When("First and second classes") {
            val givenClasses = listOf(CLASSES_1, CLASSES_2)
            Then("Check no windows") {
                givenClasses.withWindows() shouldBe givenClasses
            }
        }
        When("First and third classes") {
            val givenClasses = listOf(CLASSES_1, CLASSES_3)
            Then("Check has window") {
                givenClasses.withWindows().shouldContainExactly(
                    CLASSES_1,
                    WindowItem(CLASSES_1.time.end, CLASSES_3.time.start),
                    CLASSES_3
                )
            }
        }
        When("Second and third classes") {
            val givenClasses = listOf(CLASSES_2, CLASSES_3)
            Then("Check has window") {
                givenClasses.withWindows() shouldBe givenClasses
            }
        }
        When("First, third and fifth classes") {
            val givenClasses = listOf(CLASSES_1, CLASSES_3, CLASSES_5)
            Then("Check has two windows") {
                givenClasses.withWindows().shouldContainExactly(
                    CLASSES_1,
                    WindowItem(CLASSES_1.time.end, CLASSES_3.time.start),
                    CLASSES_3,
                    WindowItem(CLASSES_3.time.end, CLASSES_5.time.start),
                    CLASSES_5
                )
            }
        }
    }
    Given("Progress preview") {
        val givenClasses = listOf<Classes>(
            CLASSES_1,
            CLASSES_2,
            CLASSES_3
        )
        When("Time before first classes") {
            val givenClassesCopy = givenClasses.map { it.copy() }
            Then("No progress") {
                check(
                    givenClassesCopy
                        .withProgressPreview(nowTime = LocalTime.of(9, 0))
                        .all { (it as Classes).progress == null }
                )
            }
        }
        When("Time in first classes") {
            val givenClassesCopy = givenClasses.map { it.copy() }
            Then("First classes progress not null") {
                check(
                    givenClassesCopy
                        .withProgressPreview(nowTime = LocalTime.of(9, 30))
                        .all {
                            if ((it as Classes).number == 1) {
                                it.progress != null
                            } else {
                                it.progress == null
                            }
                        }
                )
            }
        }
        When("Time between first and second classes") {
            val givenClassesCopy = givenClasses.map { it.copy() }
            Then("No progress") {
                check(
                    givenClassesCopy
                        .withProgressPreview(nowTime = LocalTime.of(11, 0))
                        .all { (it as Classes).progress == null }
                )
            }
        }
        When("Time is starting third classes (at 13:45)") {
            val currentTime = LocalTime.of(13, 45)
            val givenClassesCopy = givenClasses.map { it.copy() }
            Then("Third classes has 0% progress") {
                check(
                    givenClassesCopy
                        .withProgressPreview(nowTime = currentTime)
                        .all {
                            if ((it as Classes).number == 3) {
                                it.progress == 0f
                            } else {
                                it.progress == null
                            }
                        }
                )
            }
        }
        When("Time is ending third classes (at 15:20)") {
            val currentTime = LocalTime.of(15, 20)
            val givenClassesCopy = givenClasses.map { it.copy() }
            Then("Third classes has 100% progress") {
                check(
                    givenClassesCopy
                        .withProgressPreview(nowTime = currentTime)
                        .all {
                            if ((it as Classes).number == 3) {
                                it.progress == 1f
                            } else {
                                it.progress == null
                            }
                        }
                )
            }
        }
    }
}) {

    private companion object {

        private val TIME = Time(LocalTime.of(12, 45), LocalTime.of(13, 45))
        private val CLASSES_1 = Classes(
            name = "one", number = 1,
            time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)),
            type = ClassesType.LECTURE,
            rawType = "lecture",
            place = "",
            groups = "",
            person = "Mr. Brightside",
            attachedNotePreview = null,
            progress = null,
        )
        private val CLASSES_2 = Classes(
            name = "two", number = 2,
            time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
            type = ClassesType.LECTURE,
            rawType = "lecture",
            place = "",
            groups = "",
            person = "Mr. Brightside",
            attachedNotePreview = null,
            progress = null,
        )
        private val CLASSES_3 = Classes(
            name = "three", number = 3,
            time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20)),
            type = ClassesType.LECTURE,
            rawType = "lecture",
            place = "",
            groups = "",
            person = "Mr. Brightside",
            attachedNotePreview = null,
            progress = null,
        )
        private val CLASSES_4 = Classes(
            name = "four",
            number = 4,
            time = Time(start = LocalTime.now(), end = LocalTime.now()),
            type = ClassesType.LECTURE,
            rawType = "lecture",
            place = "",
            groups = "",
            person = "Mr. Brightside",
            attachedNotePreview = null,
            progress = null,
        )
        private val CLASSES_5 = Classes(
            name = "five",
            number = 5,
            time = Time(start = LocalTime.now(), end = LocalTime.now()),
            type = ClassesType.LECTURE,
            rawType = "lecture",
            place = "",
            groups = "",
            person = "Mr. Brightside",
            attachedNotePreview = null,
            progress = null,
        )
    }
}
