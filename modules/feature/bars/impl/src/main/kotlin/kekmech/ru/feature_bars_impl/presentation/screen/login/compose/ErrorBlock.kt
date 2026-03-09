package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ErrorBlock(
    throwable: Throwable,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            appendLine("Произошла ошибка во время загрузки данных.")
            if (throwable is ExceptionWithId) {
                append("Идентификатор ошибки: ")
                appendLine(throwable.uuid)
            }
            append("Если ошибка повторяется, можете сообщить о ней по почте ")
            withLink(
                LinkAnnotation.Url(
                    url = "mailto:antonkolomeytsev@gmail.com",
                    styles = TextLinkStyles(SpanStyle(MpeixTheme.palette.primary)),
                )
            ) {
                append("antonkolomeytsev@gmail.com")
            }
        },
        style = MpeixTheme.typography.paragraphBig,
        color = MpeixTheme.palette.redMarkColor,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ErrorBlockPreview() {
    MpeixTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            ErrorBlock(
                throwable = remember { ExceptionWithId(RuntimeException()) },
            )
        }
    }
}