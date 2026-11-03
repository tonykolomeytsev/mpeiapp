package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.typography.RobotoFontFamily
import kekmech.ru.res_strings.R.string as Strings

private val LegacyH3 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 23.sp,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
)

@Composable
internal fun LoginPrompt(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp),
    ) {
        Text(
            text = stringResource(Strings.bars_stub_login_header),
            style = LegacyH3,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = stringResource(Strings.bars_stub_login_description),
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurface,
        )
        Button(
            text = stringResource(Strings.bars_stub_login_button),
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun LoginPromptPreview() {
    MpeixTheme {
        LoginPrompt(
            onLoginClick = { /* no-op */ },
        )
    }
}