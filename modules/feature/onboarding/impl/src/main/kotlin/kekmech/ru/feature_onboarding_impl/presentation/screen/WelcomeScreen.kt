package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_onboarding_impl.R
import kekmech.ru.feature_onboarding_impl.presentation.screen.compose.PromoPager
import kekmech.ru.feature_onboarding_impl.presentation.screen.compose.SettingsBlock
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun WelcomeScreen(
    darkThemeEnabled: Boolean,
    languageCode: String,
    onToggleDarkTheme: (Boolean) -> Unit,
    onClickLanguage: () -> Unit,
    onContinueClick: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp),
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Image(
                painter = painterResource(R.drawable.gradient_logo),
                contentDescription = null,
                modifier = Modifier.size(width = 150.dp, height = 60.dp),
            )
            PromoPager()
        }
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