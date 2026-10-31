package kekmech.ru.mpeiapp.demo.screens.colors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.serialization.Serializable

@Serializable
internal object ColorsScreen : NavScreen {

    @Composable
    override fun Content() {
        ColorsScreen()
    }
}

@Suppress("LongMethod")
@Composable
private fun ColorsScreen() {
    UiKitScreen(title = "Colors") { innerPadding ->

        val cells = arrayOf(
            ColorCell(
                color = MpeixTheme.palette.primary,
                name = "Primary",
            ),
            ColorCell(
                color = MpeixTheme.palette.onPrimary,
                name = "OnPrimary",
            ),
            ColorCell(
                color = MpeixTheme.palette.secondary,
                name = "Secondary",
            ),
            ColorCell(
                color = MpeixTheme.palette.onSecondary,
                name = "OnSecondary",
            ),
            ColorCell(
                color = MpeixTheme.palette.surface,
                name = "Surface",
            ),
            ColorCell(
                color = MpeixTheme.palette.surfaceContainer,
                name = "SurfaceContainer",
            ),
            ColorCell(
                color = MpeixTheme.palette.surfaceContainerHighest,
                name = "SurfaceContainerHighest",
            ),
            ColorCell(
                color = MpeixTheme.palette.onSurface,
                name = "OnSurface",
            ),
            ColorCell(
                color = MpeixTheme.palette.onSurfaceVariant,
                name = "OnSurfaceVariant",
            ),
            ColorCell(
                color = MpeixTheme.palette.outline,
                name = "Outline",
            ),
            ColorCell(
                color = MpeixTheme.palette.outlineVariant,
                name = "OutlineVariant",
            ),
            ColorCell(
                color = MpeixTheme.palette.error,
                name = "Error",
            ),
            ColorCell(
                color = MpeixTheme.palette.onError,
                name = "OnError",
            ),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(cells) { cell ->
                ColorItem(cell = cell)
            }
        }
    }
}

@Immutable
private class ColorCell(
    val color: Color,
    val name: String,
)

@Composable
@NonRestartableComposable
private fun ColorItem(cell: ColorCell) {
    Card(
        modifier = Modifier.height(100.dp),
        colors = CardDefaults.outlinedCardColors(),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.outlinedCardElevation(),
        onClick = { /* no-op */ },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .drawBehind { drawRect(cell.color) },
        )
        Text(
            text = cell.name,
            style = MpeixTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Preview
@Composable
private fun ColorItemPreview() {
    MpeixTheme {
        ColorItem(
            cell = ColorCell(
                color = MpeixTheme.palette.primary,
                name = "Primary",
            )
        )
    }
}
