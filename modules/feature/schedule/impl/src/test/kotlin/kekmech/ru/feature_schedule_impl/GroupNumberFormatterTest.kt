package kekmech.ru.feature_schedule_impl

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.utils.GroupNumberFormatter

class GroupNumberFormatterTest : BehaviorSpec({
    val formatter = GroupNumberFormatter(listOf("С", "ЭЛ", "А", "ИЭэ"))

    fun format(text: String) = formatter.format(TextFieldValue(text, TextRange(text.length)))

    Given("Group number") {
        Then("Capitalizes faculty label") {
            format("эл-12-16") shouldBe TextFieldValue("ЭЛ-12-16", TextRange(8))
        }
        Then("Prefers longest faculty label") {
            format("иээ-12-16").text shouldBe "ИЭэ-12-16"
        }
        Then("Lowercases label modifiers") {
            format("С-12М-16").text shouldBe "С-12м-16"
        }
        Then("Adds leading zero to course number and moves cursor") {
            format("С-6-") shouldBe TextFieldValue("С-06-", TextRange(5))
        }
        Then("Keeps already formatted value untouched") {
            val value = TextFieldValue("С-12-16", TextRange(3))
            formatter.format(value) shouldBe value
        }
    }
    Given("Person name") {
        Then("Keeps it untouched") {
            format("Иванов Иван").text shouldBe "Иванов Иван"
        }
    }
})
