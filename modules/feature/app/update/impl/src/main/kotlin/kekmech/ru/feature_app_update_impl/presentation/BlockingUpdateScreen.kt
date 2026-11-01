package kekmech.ru.feature_app_update_impl.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_update_impl.R
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun BlockingUpdateScreen(
    link: String,
    onExitClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onLinkClick: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    text = stringResource(Strings.blocking_update_exit),
                    onClick = onExitClick,
                    modifier = Modifier.fillMaxWidth(),
                    palette = ButtonPalette.Secondary,
                )
                Button(
                    text = stringResource(Strings.blocking_update_update),
                    onClick = onUpdateClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.ic_ill_force_update),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(Strings.blocking_update_title),
                modifier = Modifier.fillMaxWidth(),
                style = MpeixTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(Strings.blocking_update_description),
                modifier = Modifier.fillMaxWidth(),
                style = MpeixTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = link,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLinkClick)
                    .padding(vertical = 8.dp),
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.primary,
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
@PreviewLightDark
private fun BlockingUpdateScreenPreview() {
    MpeixPreview {
        BlockingUpdateScreen(
            link = "https://github.com/tonykolomeytsev/mpeiapp/issues",
            onExitClick = { /* no-op */ },
            onUpdateClick = { /* no-op */ },
            onLinkClick = { /* no-op */ },
        )
    }
}
