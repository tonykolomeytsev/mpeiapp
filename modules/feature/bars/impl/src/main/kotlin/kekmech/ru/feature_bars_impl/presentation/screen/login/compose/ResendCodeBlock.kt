package kekmech.ru.feature_bars_impl.presentation.screen.login.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeState
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ResendCodeBlock(
    providers: List<CodeProvider>,
    codeState: CodeState,
    onClickResend: (CodeProvider) -> Unit,
) {
    when (codeState) {
        is CodeState.Initial -> {
            /* no-op */
        }

        is CodeState.CodeSent -> {
            if (codeState.resendDebounceSec > 1) {
                Text(
                    text = buildAnnotatedString {
                        append("Код был отправлен в ")
                        append(codeState.provider.humanReadableName())
                        append(".\nОтправить код повторно можно будет через ")
                        withStyle(SpanStyle()) {
                            append("${codeState.resendDebounceSec} секунд")
                        }
                    },
                    style = MpeixTheme.typography.paragraphBig,
                    color = MpeixTheme.palette.content,
                )
            } else {
                val clickableSpanStyle = SpanStyle(
                    color = MpeixTheme.palette.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                )
                Text(
                    text = buildAnnotatedString {
                        append("Отправить код повторно")
                        providers.fastForEachIndexed { i, provider ->
                            if (i > 0) {
                                if (i == providers.lastIndex) {
                                    append(" или в ")
                                } else {
                                    append(", в ")
                                }
                            } else {
                                append(" в ")
                            }
                            withLink(
                                LinkAnnotation.Clickable(
                                    tag = provider.name,
                                    styles = TextLinkStyles(style = clickableSpanStyle),
                                    linkInteractionListener = {
                                        onClickResend.invoke(provider)
                                    }
                                )
                            ) {
                                append(provider.humanReadableName())
                            }
                        }
                    },
                    style = MpeixTheme.typography.paragraphBig,
                    color = MpeixTheme.palette.content,
                )
            }
        }

        is CodeState.SendingCode -> {
            Text(
                text = "Отправляем код...",
                style = MpeixTheme.typography.paragraphBig,
                color = MpeixTheme.palette.content,
            )
        }
    }
}

@Composable
private fun CodeProvider.humanReadableName(): String =
    when (this) {
        CodeProvider.MAX -> "MAX"
        CodeProvider.VK -> "Вконтакте"
        CodeProvider.TG -> "Telegram"
    }

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ResendCodeBlockPreview() {
    MpeixTheme {
        var remainingSec by remember { mutableIntStateOf(15) }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            ResendCodeBlock(
                providers = listOf(CodeProvider.TG, CodeProvider.VK, CodeProvider.MAX),
                codeState = CodeState.SendingCode(CodeProvider.TG),
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.TG, CodeProvider.VK, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = remainingSec,
                ),
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.TG, CodeProvider.VK, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = 0,
                ),
                onClickResend = { remainingSec-- },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.VK, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = 0,
                ),
                onClickResend = { remainingSec-- },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.VK),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = 0,
                ),
                onClickResend = { remainingSec-- },
            )
        }
    }
}
