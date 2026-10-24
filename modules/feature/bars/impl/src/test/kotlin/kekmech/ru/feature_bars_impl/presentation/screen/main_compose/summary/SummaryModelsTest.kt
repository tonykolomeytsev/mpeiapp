package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType

private fun km(
    mark: Float = NO_MARK,
    week: String = "8",
    raw: String = "",
    name: String = "КМ",
    weight: String = "20",
) = ControlActivity(name = name, weight = weight, deadline = week, finalMark = mark, rawMark = raw)

class SummaryModelsTest : BehaviorSpec({

    Given("a control activity with a passing mark") {
        Then("it is PASSED regardless of the week") {
            km(mark = 4f).resolveStatus(currentWeek = 1) shouldBe KmStatus.PASSED
            km(mark = 4f).resolveStatus(currentWeek = 15) shouldBe KmStatus.PASSED
            km(mark = 2.5f).resolveStatus(currentWeek = null) shouldBe KmStatus.PASSED
        }
        Then("it is RETAKE when BARS marks it as a retake") {
            km(mark = 4f, raw = "4 (пересдача 12.12.2025)").resolveStatus(currentWeek = 15) shouldBe KmStatus.RETAKE
        }
    }

    Given("a control activity with a failing mark") {
        Then("it needs a retake") {
            km(mark = 2f).resolveStatus(currentWeek = 15) shouldBe KmStatus.RETAKE
            km(mark = 0f).resolveStatus(currentWeek = 3) shouldBe KmStatus.RETAKE
        }
    }

    Given("a control activity without a mark") {
        Then("it is DEBT only after its control week") {
            km(week = "8").resolveStatus(currentWeek = 9) shouldBe KmStatus.DEBT
            km(week = "8").resolveStatus(currentWeek = 8) shouldBe KmStatus.UPCOMING
            km(week = "8").resolveStatus(currentWeek = 3) shouldBe KmStatus.UPCOMING
        }
        Then("it is UPCOMING when the week is not known") {
            km(week = "").resolveStatus(currentWeek = 9) shouldBe KmStatus.UPCOMING
            km(week = "8").resolveStatus(currentWeek = null) shouldBe KmStatus.UPCOMING
        }
    }

    Given("a discipline") {
        val discipline = AssessedDiscipline(
            name = "Физика",
            person = "Иванов И.И.",
            assessmentType = "Экзамен",
            controlActivities = listOf(
                km(mark = 5f, name = "Контрольная"),
                km(name = "", weight = "", week = ""),
                km(mark = 2f, week = "4"),
            ),
            finalGrades = listOf(
                FinalGrade("Текущий балл", 74.4f, FinalGradeType.CURRENT_SCORE),
                FinalGrade("Итог", NO_MARK, FinalGradeType.FINAL_MARK),
            ),
        )

        When("converted to the summary") {
            val ui = listOf(discipline).toSummary(currentWeek = 10).single()

            Then("blank names get a numbered title") {
                ui.kms.map { it.title } shouldBe listOf("Контрольная", "КМ 2", "КМ")
            }
            Then("blank weight is hidden") {
                ui.kms.map { it.weight } shouldBe listOf("20", null, "20")
            }
            Then("only existing scores are listed, rounded") {
                ui.scores shouldBe listOf(ScoreUi("Текущий балл", 74))
            }
            Then("passed counter counts passing marks only") {
                ui.passedCount shouldBe 1
            }
            Then("statuses are resolved against the current week") {
                ui.kms.map { it.status } shouldBe listOf(KmStatus.PASSED, KmStatus.UPCOMING, KmStatus.RETAKE)
            }
        }
    }
})
