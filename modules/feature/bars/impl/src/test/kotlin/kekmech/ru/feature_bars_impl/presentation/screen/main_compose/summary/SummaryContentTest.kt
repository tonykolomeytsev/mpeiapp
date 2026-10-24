package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.summary

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.domain.FinalGrade
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.color.DarkMpeixPalette
import kekmech.ru.ui_theme.color.LightMpeixPalette
import kekmech.ru.ui_theme.color.MpeixPalette
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

/**
 * UI tests of the BARS summary: light / dark theme x phone / tablet widths.
 * They run on the JVM (Robolectric) together with `./gradlew test`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SummaryContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val discipline = AssessedDiscipline(
        name = "Теория принятия решений",
        person = "Еремеев А.П.",
        assessmentType = "Экзамен",
        controlActivities = listOf(
            ControlActivity(name = "КМ-1 Контрольная", weight = "20", deadline = "4", finalMark = 5f),
            ControlActivity(name = "КМ-2 Лабораторная", weight = "20", deadline = "6", finalMark = 2f),
            ControlActivity(name = "КМ-3 Домашнее задание", weight = "30", deadline = "7", finalMark = -1f),
            ControlActivity(name = "КМ-4 Коллоквиум", weight = "30", deadline = "15", finalMark = -1f),
        ),
        finalGrades = listOf(FinalGrade("Текущий балл", 74f, FinalGradeType.CURRENT_SCORE)),
    )

    /** Week 10: КМ-3 (week 7, no mark) is overdue, КМ-4 (week 15) is ahead */
    private val summary = listOf(discipline).toSummary(currentWeek = 10).single()

    private var palette: MpeixPalette? = null

    private fun showCard(darkTheme: Boolean, width: Dp = 360.dp) {
        composeRule.setContent {
            Box(Modifier.requiredSize(width, 900.dp)) {
                MpeixTheme(darkTheme = darkTheme) {
                    palette = MpeixTheme.palette
                    var expanded by remember { mutableStateOf(false) }
                    SummaryDisciplineCard(
                        discipline = summary,
                        isExpanded = expanded,
                        onClick = { expanded = !expanded },
                        modifier = Modifier.testTag(CARD_TAG),
                    )
                }
            }
        }
    }

    private fun assertCollapsedThenExpand() {
        composeRule.onNodeWithText(discipline.name).assertExists()
        composeRule.onNodeWithText(context.getString(Strings.bars_summary_km_progress, 1, 4)).assertExists()
        // collapsed: the control activities are not composed
        composeRule.onAllNodesWithText("КМ-1 Контрольная").assertCountEquals(0)

        composeRule.onNodeWithText(discipline.name).performClick()

        discipline.controlActivities.forEach {
            composeRule.onNodeWithText(it.name).assertExists()
        }
        // every status has a label, color is never the only signal
        listOf(
            Strings.bars_km_status_passed,
            Strings.bars_km_status_retake,
            Strings.bars_km_status_debt,
            Strings.bars_km_status_upcoming,
        ).forEach { composeRule.onNodeWithText(context.getString(it)).assertExists() }
    }

    @Test
    fun card_light_phone() {
        showCard(darkTheme = false)
        assertCollapsedThenExpand()
        assertEquals(LightMpeixPalette.surface, palette?.surface)
    }

    @Test
    fun card_dark_phone() {
        showCard(darkTheme = true)
        assertCollapsedThenExpand()
        assertEquals(DarkMpeixPalette.surface, palette?.surface)
    }

    @Test
    fun card_light_tablet() {
        showCard(darkTheme = false, width = 520.dp)
        assertCollapsedThenExpand()
        composeRule.onNodeWithTag(CARD_TAG).assertWidthIsEqualTo(520.dp)
    }

    @Test
    fun card_dark_tablet() {
        showCard(darkTheme = true, width = 520.dp)
        assertCollapsedThenExpand()
        composeRule.onNodeWithTag(CARD_TAG).assertWidthIsEqualTo(520.dp)
    }

    @Test
    fun error_networkAndParsingFailuresAreExplainedDifferently_andCanBeRetried() {
        var retries = 0
        var failure: Throwable by mutableStateOf<Throwable>(ExceptionWithId(IOException("offline")))
        composeRule.setContent {
            MpeixTheme(darkTheme = false) {
                SummaryError(throwable = failure, onRetry = { retries++ })
            }
        }

        composeRule.onNodeWithText(context.getString(Strings.bars_summary_error_network_title)).assertExists()

        failure = ExceptionWithId(IllegalStateException("unexpected json"))
        composeRule.waitForIdle()
        composeRule.onNodeWithText(context.getString(Strings.bars_summary_error_parsing_title)).assertExists()

        composeRule.onNodeWithText(context.getString(Strings.bars_summary_error_retry)).performClick()
        assertEquals(1, retries)
    }

    @Test
    fun networkErrorsAreRecognized() {
        assertTrue(IOException().isNetworkError())
        assertTrue(ExceptionWithId(IOException()).isNetworkError())
        assertTrue(IllegalStateException("BARS request timeout: user.getMarks").isNetworkError())
        assertFalse(IllegalStateException("Unexpected JSON token").isNetworkError())
    }

    private companion object {
        const val CARD_TAG = "summary_card"
    }
}
