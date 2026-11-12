package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_onboarding_impl.R
import kekmech.ru.feature_onboarding_impl.presentation.screen.compose.PromoPager
import kekmech.ru.feature_onboarding_impl.presentation.screen.compose.SettingsBlock
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private val ControlsPaneWidth = 360.dp

@Composable
internal fun WelcomeScreen(
    darkThemeEnabled: Boolean,
    languageCode: String,
    onToggleDarkTheme: (Boolean) -> Unit,
    onClickLanguage: () -> Unit,
    onContinueClick: () -> Unit,
) {
    val controls = @Composable { modifier: Modifier ->
        WelcomeControls(
            darkThemeEnabled = darkThemeEnabled,
            languageCode = languageCode,
            onToggleDarkTheme = onToggleDarkTheme,
            onClickLanguage = onClickLanguage,
            onContinueClick = onContinueClick,
            modifier = modifier,
        )
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (maxWidth > maxHeight) {
                LandscapeLayout(controls)
            } else {
                PortraitLayout(controls)
            }
        }
    }
}

/**
 * Logo and promo pager on top, settings and the start button pinned to the bottom.
 */
@Composable
private fun PortraitLayout(controls: @Composable (Modifier) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            WelcomeLogo()
            PromoPager(Modifier.weight(1f, fill = false))
        }
        controls(
            Modifier
                .adaptiveContentWidth()
                .padding(16.dp),
        )
    }
}

/**
 * Promo pager on the left, logo, settings and the start button on the right,
 * so that both fit into the short landscape height.
 */
@Composable
private fun LandscapeLayout(controls: @Composable (Modifier) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PromoPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 16.dp),
        )
        Column(
            modifier = Modifier
                .width(ControlsPaneWidth)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            WelcomeLogo()
            controls(Modifier)
        }
    }
}

@Composable
private fun WelcomeLogo() {
    Image(
        painter = painterResource(R.drawable.gradient_logo),
        contentDescription = null,
        modifier = Modifier.size(width = 150.dp, height = 60.dp),
    )
}

@Composable
private fun WelcomeControls(
    darkThemeEnabled: Boolean,
    languageCode: String,
    onToggleDarkTheme: (Boolean) -> Unit,
    onClickLanguage: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsBlock(
            darkThemeEnabled = darkThemeEnabled,
            languageCode = languageCode,
            onToggleDarkTheme = onToggleDarkTheme,
            onClickLanguage = onClickLanguage,
        )
        Button(
            text = stringResource(Strings.onboarding_welcome_start_using),
            onClick = onContinueClick,
            Modifier.fillMaxWidth()
        )
    }
}

@Composable
@PreviewLightDark
private fun WelcomeScreenPreview() {
    MpeixTheme {
        WelcomeScreen(
            darkThemeEnabled = isSystemInDarkTheme(),
            languageCode = "ru_RU",
            onToggleDarkTheme = { /* no-op */ },
            onClickLanguage = { /* no-op */ },
            onContinueClick = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = "spec:width=891dp,height=411dp")
private fun WelcomeScreenLandscapePreview() {
    WelcomeScreenPreview()
}

@Composable
@Preview(device = "spec:width=640dp,height=360dp")
private fun WelcomeScreenSmallLandscapePreview() {
    WelcomeScreenPreview()
}
