package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun CompletedLayout() {
    Scaffold(
        // The screen closes by itself in a moment, nothing to navigate to
        topBar = { TopAppBar(navigationIcon = {}) },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Spacer(Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.adaptiveContentWidth(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MpeixTheme.palette.primary,
                    modifier = Modifier.size(72.dp),
                )
                Text(
                    text = "Готово!",
                    style = MpeixTheme.typography.titleLarge,
                    color = MpeixTheme.palette.onSurface,
                )
                Text(
                    text = "Вы вошли в БАРС",
                    style = MpeixTheme.typography.bodyLarge,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun CompletedPreview() {
    CompletedLayout()
}
