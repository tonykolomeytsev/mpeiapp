package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.typography.RobotoFontFamily
import kekmech.ru.res_strings.R.string as Strings

private val LegacyH3 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 23.sp,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
)

private val LegacyH4 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 22.sp,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
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
            color = MpeixTheme.palette.content,
        )
        Text(
            text = stringResource(Strings.bars_stub_login_description),
            style = MpeixTheme.typography.paragraphNormal,
            color = MpeixTheme.palette.content,
        )
        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) {
            Text(
                text = stringResource(Strings.bars_stub_login_button),
                style = LegacyH4,
            )
        }
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