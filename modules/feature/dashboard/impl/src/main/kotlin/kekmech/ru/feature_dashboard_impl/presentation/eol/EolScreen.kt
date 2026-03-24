package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.ui_kit_topappbar.TopAppBar

@Composable
internal fun EolScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "От разработчика",
                navigationIcon = { BackIconButton(onBackClick) }
            )
        },
        contentColor = MpeixTheme.palette.content,
        containerColor = MpeixTheme.palette.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(
                        "Всем привет!\nНа связи Антон — выпускник С-12-16 и единственный разработчик этого приложения.\n\n" +
                                "За 6 лет существования MpeiX я ни разу не публиковал личных обращений, " +
                                "но мы дожили до пиздеца, поэтому я должен вас предупредить.\n\n" +
                                "Существует риск введения «белых списков» в Москве даже на проводном интернете. " +
                                "Если риск реализуется, то перестанут быть доступны Google Play, сервер MpeiX и сайт МЭИ. " +
                                "В этом случае вы не сможете ни обновить приложение, ни даже нормально использовать то, " +
                                "что от него останется.\n\n"
                    )
                    append(
                        "Я конечно же попробую:\n" +
                                "- продублировать MpeiX в RuStore,\n" +
                                "- пробить дырку в «коричневых списках» до всех нужных сайтов, " +
                                "от которых зависит приложение.\n\n"
                    )
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Но чудес обещать не могу\n\n")
                    }
                    append(
                        "Остаюсь сдержанным в своих выражениях, сами понимаете — свобода слова :D\n" +
                                "Следим за ситуацией."
                    )
                },
                style = MpeixTheme.typography.paragraphBig,
            )
            Image(
                painter = painterResource(R.drawable.ic_banner_eol),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
private fun EolScreenPreview() {
    MpeixTheme {
        EolScreen(onBackClick = { /* no-op */ })
    }
}