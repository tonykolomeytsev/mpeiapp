package kekmech.ru.feature_dashboard_impl.presentation.eol

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.width
import androidx.test.core.app.ApplicationProvider
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.color.DarkMpeixPalette
import kekmech.ru.ui_theme.color.LightMpeixPalette
import kekmech.ru.ui_theme.color.MpeixPalette
import kekmech.ru.ui_theme.theme.MpeixScreenTheme
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.theme.ScreenContentMaxWidth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests of the "About" screen: light / dark theme x phone / tablet portrait / tablet landscape.
 * They run on the JVM (Robolectric) together with `./gradlew test`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EolScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val sbpPhone = "+7 900 000-00-00"
    private val options = donationOptions(
        sbpPhone = sbpPhone,
        tbankCard = "1111 2222 3333 4444",
        tbankUrl = "https://www.tbank.ru/cf/test",
        boostyUrl = "https://boosty.to/test",
        lavaUrl = LAVA_URL,
    )

    private var palette: MpeixPalette? = null

    private fun showScreen(
        darkTheme: Boolean,
        width: Dp,
        height: Dp,
        donation: List<DonationOption> = options,
    ) {
        composeRule.setContent {
            // the window of the given size, independent of the test device
            Box(Modifier.requiredSize(width, height)) {
                MpeixScreenTheme(darkTheme = darkTheme) {
                    palette = MpeixTheme.palette
                    EolScreen(
                        onBackClick = {},
                        donationOptions = donation,
                        modifier = Modifier.testTag(ROOT_TAG),
                    )
                }
            }
        }
    }

    private fun assertContentIsLimitedInWidth() {
        val width = composeRule.onNodeWithTag(ROOT_TAG).getUnclippedBoundsInRoot().width
        assertTrue("content is $width wide, the limit is $ScreenContentMaxWidth", width <= ScreenContentMaxWidth)
    }

    private fun assertAboutAndSupportAreShown() {
        composeRule.onNodeWithText(context.getString(Strings.about_project_subtitle)).assertExists()
        composeRule.onNodeWithText(context.getString(Strings.about_features_title)).assertExists()
        composeRule.onNodeWithText(context.getString(Strings.support_title)).assertExists()
        composeRule.onNodeWithText(sbpPhone).assertExists()
    }

    // region themes x screens

    @Test
    fun phone_light() {
        showScreen(darkTheme = false, width = PHONE_W, height = PHONE_H)
        assertAboutAndSupportAreShown()
        composeRule.onNodeWithTag(ROOT_TAG).assertWidthIsEqualTo(PHONE_W)
        assertEquals(LightMpeixPalette.background, palette?.background)
    }

    @Test
    fun phone_dark() {
        showScreen(darkTheme = true, width = PHONE_W, height = PHONE_H)
        assertAboutAndSupportAreShown()
        composeRule.onNodeWithTag(ROOT_TAG).assertWidthIsEqualTo(PHONE_W)
        assertEquals(DarkMpeixPalette.background, palette?.background)
    }

    @Test
    fun tabletPortrait_light_contentIsLimitedInWidth() {
        showScreen(darkTheme = false, width = TABLET_PORTRAIT_W, height = TABLET_PORTRAIT_H)
        assertAboutAndSupportAreShown()
        assertContentIsLimitedInWidth()
    }

    @Test
    fun tabletPortrait_dark_contentIsLimitedInWidth() {
        showScreen(darkTheme = true, width = TABLET_PORTRAIT_W, height = TABLET_PORTRAIT_H)
        assertAboutAndSupportAreShown()
        assertContentIsLimitedInWidth()
    }

    @Test
    fun tabletLandscape_light_contentIsLimitedInWidth() {
        showScreen(darkTheme = false, width = TABLET_LANDSCAPE_W, height = TABLET_LANDSCAPE_H)
        assertAboutAndSupportAreShown()
        assertContentIsLimitedInWidth()
    }

    @Test
    fun tabletLandscape_dark_contentIsLimitedInWidth() {
        showScreen(darkTheme = true, width = TABLET_LANDSCAPE_W, height = TABLET_LANDSCAPE_H)
        assertAboutAndSupportAreShown()
        assertContentIsLimitedInWidth()
    }

    // endregion

    @Test
    fun supportCard_isHidden_whenNothingIsConfigured() {
        showScreen(darkTheme = false, width = PHONE_W, height = PHONE_H, donation = emptyList())
        composeRule.onNodeWithText(context.getString(Strings.about_project_subtitle)).assertExists()
        composeRule.onAllNodesWithText(context.getString(Strings.support_title)).assertCountEquals(0)
    }

    @Test
    fun tapOnRequisite_copiesIt_andShowsSnackbar() {
        showScreen(darkTheme = false, width = PHONE_W, height = PHONE_H)

        composeRule.onNodeWithText(sbpPhone).performScrollTo().performClick()

        val label = context.getString(Strings.support_method_sbp)
        val message = context.getString(Strings.support_copied, label)
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(message).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun lavaLink_isShown_andCanBeOpened() {
        showScreen(darkTheme = false, width = PHONE_W, height = PHONE_H)
        composeRule.onNodeWithText(context.getString(Strings.support_method_lava)).assertExists()
        composeRule.onNodeWithText(LAVA_URL).assertExists()
        assertEquals(LAVA_URL, options.first { it.method == DonationMethod.LAVA }.url)
    }

    @Test
    fun donationOptions_dropEmptyMethods() {
        val onlyBoosty = donationOptions(sbpPhone = " ", tbankCard = "", tbankUrl = "", boostyUrl = "https://boosty.to/x")
        assertEquals(listOf(DonationMethod.BOOSTY), onlyBoosty.map { it.method })
        assertEquals(emptyList<DonationOption>(), donationOptions("", "", "", ""))
        assertEquals(listOf(DonationMethod.LAVA), donationOptions("", "", "", "", lavaUrl = LAVA_URL).map { it.method })
    }

    private companion object {
        const val ROOT_TAG = "eol_root"
        const val LAVA_URL = "https://app.lava.top/pharosteam"
        val PHONE_W = 360.dp
        val PHONE_H = 800.dp
        val TABLET_PORTRAIT_W = 800.dp
        val TABLET_PORTRAIT_H = 1280.dp
        val TABLET_LANDSCAPE_W = 1280.dp
        val TABLET_LANDSCAPE_H = 800.dp
    }
}
