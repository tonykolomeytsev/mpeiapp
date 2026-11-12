package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun WhatsNewScreen(
    onBackClick: () -> Unit,
    content: List<WhatsNewBlock> = WhatsNewContent,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(onBackClick) },
                title = stringResource(Strings.dashboard_banner_whats_new_header),
            )
        },
        containerColor = MpeixTheme.palette.surface,
        contentColor = MpeixTheme.palette.onSurface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .adaptiveContentWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            content.forEachIndexed { index, block ->
                WhatsNewBlockItem(block = block, isFirst = index == 0)
            }
        }
    }
}

@Composable
private fun WhatsNewBlockItem(
    block: WhatsNewBlock,
    isFirst: Boolean,
) {
    when (block) {
        is WhatsNewBlock.Header -> Text(
            text = block.text,
            style = MpeixTheme.typography.titleLarge,
            // headers start a new section, so they are separated from the previous text
            modifier = Modifier.padding(top = if (isFirst) 0.dp else 12.dp),
        )

        is WhatsNewBlock.Paragraph -> Text(
            text = block.text,
            style = MpeixTheme.typography.bodyLarge,
        )

        is WhatsNewBlock.Bullet -> Row {
            Text(
                text = "•",
                style = MpeixTheme.typography.bodyLarge,
                modifier = Modifier.width(20.dp),
            )
            Text(
                text = block.text,
                style = MpeixTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
        }

        is WhatsNewBlock.Illustration -> Image(
            imageVector = block.imageVector,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(32.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun WhatsNewScreenPreview() {
    MpeixTheme {
        WhatsNewScreen(onBackClick = { /* no-op */ })
    }
}

@PreviewLightDark
@Composable
private fun WhatsNewScreenAllBlocksPreview() {
    MpeixTheme {
        WhatsNewScreen(
            onBackClick = { /* no-op */ },
            content = listOf(
                WhatsNewBlock.Paragraph(
                    buildAnnotatedString {
                        append("Обычный абзац с ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("жирным текстом") }
                        append(" внутри.")
                    }
                ),
                WhatsNewBlock.Header("Заголовок раздела"),
                WhatsNewBlock.Bullet("Короткий пункт списка"),
                WhatsNewBlock.Bullet(
                    "Длинный пункт списка, который не помещается в одну строку " +
                        "и переносится с отступом"
                ),
                WhatsNewBlock.Paragraph("Абзац после списка."),
            ),
        )
    }
}
