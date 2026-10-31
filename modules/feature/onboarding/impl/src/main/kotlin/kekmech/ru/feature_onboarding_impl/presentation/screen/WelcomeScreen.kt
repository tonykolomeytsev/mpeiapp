package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.foundation.Image
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
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun WelcomeScreen(
    onContinueClick: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            Button(
                text = stringResource(Strings.onboarding_welcome_start_using),
                onClick = onContinueClick,
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
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
            onContinueClick = { /* no-op */ },
        )
    }
}