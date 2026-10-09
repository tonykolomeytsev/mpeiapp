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
            val provider = codeState.provider
            val otherProviders = providers.filter { it != provider }

            if (provider == CodeProvider.TOTP) {
                // TOTP is generated locally in the authenticator app - not sent via network
                Text(
                    text = "Введите код подтверждения из приложения-аутентификатора (Google Authenticator, Яндекс Ключ и др.)",
                    style = MpeixTheme.typography.paragraphBig,
                    color = MpeixTheme.palette.content,
                )
                if (otherProviders.isNotEmpty()) {
                    if (codeState.resendDebounceSec > 1) {
                        Text(
                            text = "Отправить код через ${otherProviders.first().humanReadableName()} можно будет через ${codeState.resendDebounceSec} секунд",
                            style = MpeixTheme.typography.paragraphNormal,
                            color = MpeixTheme.palette.contentVariant,
                        )
                    } else {
                        val clickableSpanStyle = SpanStyle(
                            color = MpeixTheme.palette.primary,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Нет доступа к приложению? Отправить код ")
                                otherProviders.fastForEachIndexed { i, other ->
                                    if (i > 0) append(" или ")
                                    withLink(
                                        LinkAnnotation.Clickable(
                                            tag = other.name,
                                            styles = TextLinkStyles(style = clickableSpanStyle),
                                            linkInteractionListener = {
                                                onClickResend.invoke(other)
                                            }
                                        )
                                    ) {
                                        append(other.actionLinkName())
                                    }
                                }
                            },
                            style = MpeixTheme.typography.paragraphNormal,
                            color = MpeixTheme.palette.content,
                        )
                    }
                }
            } else {
                // Messenger or email delivery
                val statusText = codeState.serverMessage
                    ?: when (provider) {
                        CodeProvider.MAX -> "Код был отправлен в МАКС"
                        CodeProvider.TG -> "Код был отправлен в Telegram"
                        CodeProvider.VK -> "Код был отправлен во ВКонтакте"
                        CodeProvider.EMAIL -> "Код был отправлен на почту"
                        CodeProvider.TOTP -> "Введите код из приложения-аутентификатора"
                    }

                if (codeState.resendDebounceSec > 1) {
                    Text(
                        text = buildAnnotatedString {
                            append(statusText)
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
                            append(statusText)
                            append(".\nОтправить код повторно ")
                            providers.fastForEachIndexed { i, p ->
                                if (i > 0) {
                                    if (i == providers.lastIndex) {
                                        append(" или ")
                                    } else {
                                        append(", ")
                                    }
                                }
                                withLink(
                                    LinkAnnotation.Clickable(
                                        tag = p.name,
                                        styles = TextLinkStyles(style = clickableSpanStyle),
                                        linkInteractionListener = {
                                            onClickResend.invoke(p)
                                        }
                                    )
                                ) {
                                    append(p.actionLinkName())
                                }
                            }
                        },
                        style = MpeixTheme.typography.paragraphBig,
                        color = MpeixTheme.palette.content,
                    )
                }
            }
        }

        is CodeState.SendingCode -> {
            val text = if (codeState.provider == CodeProvider.TOTP) {
                "Ожидание кода подтверждения..."
            } else {
                "Отправляем код ${codeState.provider.actionLinkName()}..."
            }
            Text(
                text = text,
                style = MpeixTheme.typography.paragraphBig,
                color = MpeixTheme.palette.content,
            )
        }
    }
}

@Composable
private fun CodeProvider.humanReadableName(): String =
    when (this) {
        CodeProvider.TOTP -> "приложение (TOTP)"
        CodeProvider.MAX -> "МАКС"
        CodeProvider.VK -> "ВКонтакте"
        CodeProvider.TG -> "Telegram"
        CodeProvider.EMAIL -> "почту"
    }

private fun CodeProvider.actionLinkName(): String =
    when (this) {
        CodeProvider.TOTP -> "по одноразовому коду"
        CodeProvider.MAX -> "через МАКС"
        CodeProvider.VK -> "через ВКонтакте"
        CodeProvider.TG -> "через Telegram"
        CodeProvider.EMAIL -> "на почту"
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
                providers = listOf(CodeProvider.TOTP, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.TOTP,
                    resendDebounceSec = 0,
                ),
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.TOTP, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = remainingSec,
                ),
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(CodeProvider.TOTP, CodeProvider.MAX),
                codeState = CodeState.CodeSent(
                    provider = CodeProvider.MAX,
                    resendDebounceSec = 0,
                ),
                onClickResend = { remainingSec-- },
            )
        }
    }
}
