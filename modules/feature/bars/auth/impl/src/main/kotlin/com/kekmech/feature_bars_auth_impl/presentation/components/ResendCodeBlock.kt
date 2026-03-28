package com.kekmech.feature_bars_auth_impl.presentation.components

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
import androidx.compose.ui.text.AnnotatedString
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
import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.CodeState
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ResendCodeBlock(
    providers: List<Auth2faProvider>,
    codeState: CodeState,
    debounceSec: Int,
    onClickResend: (Auth2faProvider) -> Unit,
) {
    val clickableSpanStyle = SpanStyle(
        color = MpeixTheme.palette.primary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
    )

    when (codeState) {
        is CodeState.Initial -> {
            Text(
                text = if (providers.isNotEmpty()) {
                    buildAnnotatedString {
                        append("Отправить код")
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
                                append(provider.name)
                            }
                        }
                    }
                } else {
                    AnnotatedString("Загрузка...")
                },
                style = MpeixTheme.typography.paragraphBig,
                color = MpeixTheme.palette.content,
            )
        }

        is CodeState.CodeSent -> {
            if (debounceSec > 1) {
                Text(
                    text = buildAnnotatedString {
                        append("Код был отправлен в ")
                        append(codeState.provider.name)
                        append(".\nОтправить код повторно можно будет через ")
                        withStyle(SpanStyle()) {
                            append("${debounceSec} секунд")
                        }
                    },
                    style = MpeixTheme.typography.paragraphBig,
                    color = MpeixTheme.palette.content,
                )
            } else {
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
                                append(provider.name)
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
                text = "Отправляем код в ${codeState.provider.name}...",
                style = MpeixTheme.typography.paragraphBig,
                color = MpeixTheme.palette.content,
            )
        }
    }
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
                providers = listOf(Auth2faProviderTG, Auth2faProviderVK, Auth2faProviderSKAM),
                codeState = CodeState.SendingCode(Auth2faProviderTG),
                debounceSec = 15,
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(Auth2faProviderTG, Auth2faProviderVK, Auth2faProviderSKAM),
                codeState = CodeState.CodeSent(
                    provider = Auth2faProviderSKAM,
                ),
                debounceSec = 15,
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(Auth2faProviderTG, Auth2faProviderVK, Auth2faProviderSKAM),
                codeState = CodeState.CodeSent(
                    provider = Auth2faProviderSKAM,
                ),
                debounceSec = 0,
                onClickResend = { /* no-op */ },
            )
            ResendCodeBlock(
                providers = listOf(Auth2faProviderTG, Auth2faProviderVK, Auth2faProviderSKAM),
                codeState = CodeState.CodeSent(
                    provider = Auth2faProviderSKAM,
                ),
                debounceSec = -1,
                onClickResend = { remainingSec-- },
            )
            ResendCodeBlock(
                providers = listOf(Auth2faProviderVK, Auth2faProviderSKAM),
                codeState = CodeState.CodeSent(
                    provider = Auth2faProviderSKAM,
                ),
                debounceSec = -2,
                onClickResend = { remainingSec-- },
            )
            ResendCodeBlock(
                providers = listOf(Auth2faProviderVK),
                codeState = CodeState.CodeSent(
                    provider = Auth2faProviderSKAM,
                ),
                debounceSec = -3,
                onClickResend = { remainingSec-- },
            )
        }
    }
}

private val Auth2faProviderSKAM = Auth2faProvider("3", "Skam", true)
private val Auth2faProviderVK = Auth2faProvider("2", "Вконтакте", false)
private val Auth2faProviderTG = Auth2faProvider("1", "Telegram", false)
