package com.kekmech.feature_bars_auth_impl.presentation.screens.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.ui_preview.MpeixPreview
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.ui_theme.typography.RobotoFontFamily

private val LegacyH3 = TextStyle(
    fontFamily = RobotoFontFamily,
    lineHeight = 23.sp,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
)

@Composable
internal fun ErrorScreen() {
    val router = LocalRouter.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Вход в БАРС МЭИ",
                navigationIcon = {
                    BackIconButton {
                        router.executeCommand(PopBackStack())
                    }
                },
            )
        },
        containerColor = MpeixTheme.palette.background,
        contentColor = MpeixTheme.palette.content,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "Неожиданная ошибка",
                style = LegacyH3,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            )
            Text(
                text = "От сайта МЭИ пришел неожиданный ответ, который приложение не может обработать.",
                style = MpeixTheme.typography.paragraphBig,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = buildAnnotatedString {
                    append("Если ошибка повторяется, пожалуйста, сообщите о ней разработчику по почте: ")
                    withLink(
                        LinkAnnotation.Url(
                            url = "mailto:antonkolomeytsev@gmail.com",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MpeixTheme.palette.primary,
                                    fontWeight = FontWeight.Medium,
                                ),
                            ),
                        )
                    ) {
                        append("antonkolomeytsev@gmail.com")
                    }
                },
                style = MpeixTheme.typography.paragraphBig,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    MpeixPreview {
        ErrorScreen()
    }
}