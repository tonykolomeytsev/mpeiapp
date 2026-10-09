package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun EolScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "О проекте MPEI Next",
                navigationIcon = { BackIconButton(onBackClick) }
            )
        },
        contentColor = MpeixTheme.palette.content,
        containerColor = MpeixTheme.palette.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MpeixTheme.palette.primaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚀 MPEI Next — Новое поколение",
                        style = MpeixTheme.typography.header3.copy(
                            fontWeight = FontWeight.Bold,
                            color = MpeixTheme.palette.content,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Проект получил второе дыхание: автономная работа, прямое подключение к порталу МЭИ, современные офлайн-карты и обновлённый дизайн.",
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.content,
                            lineHeight = 20.sp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "✨ Ключевые возможности:",
                style = MpeixTheme.typography.header4.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            AboutFeatureItem(
                title = "📅 Прямой парсер расписания МЭИ",
                desc = "Больше нет зависимости от сторонних серверов. Расписание парсится напрямую с официального портала mpei.ru.",
            )
            AboutFeatureItem(
                title = "🗺 Офлайн-карты OpenStreetMap",
                desc = "Все корпуса (А, Б, В, Г, Д, Е, Ж, З, И, К, М, Р, С, Т, Ч, Э), кафедры, столовые и общежития кампуса Лефортово работают полностью автономно.",
            )
            AboutFeatureItem(
                title = "🎨 Современный дизайн",
                desc = "Интерфейс на Jetpack Compose, плавные карточки с типами пар, виджеты и полная поддержка Android 14+.",
            )
            AboutFeatureItem(
                title = "🌐 Открытый исходный код",
                desc = "Разработка ведётся открыто сообществом студентов МЭИ на GitHub.",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.ic_banner_open_source),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AboutFeatureItem(title: String, desc: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MpeixTheme.palette.surfacePlus1,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MpeixTheme.typography.paragraphNormal.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = MpeixTheme.palette.contentVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
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