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
                color = MpeixTheme.palette.secondary,
                name = "Secondary",
            ),
            ColorCell(
                color = MpeixTheme.palette.tertiary,
                name = "Tertiary",
            ),
            ColorCell(
                color = MpeixTheme.palette.background,
                name = "Background",
            ),
            ColorCell(
                color = MpeixTheme.palette.surface,
                name = "Surface",
            ),
            ColorCell(
                color = MpeixTheme.palette.surfacePlus1,
                name = "Surface+1",
            ),
            ColorCell(
                color = MpeixTheme.palette.surfacePlus2,
                name = "Surface+2",
            ),
            ColorCell(
                color = MpeixTheme.palette.surfacePlus3,
                name = "Surface+3",
            ),
            ColorCell(
                color = MpeixTheme.palette.primaryContainer,
                name = "PrimaryContainer",
            ),
            ColorCell(
                color = MpeixTheme.palette.secondaryContainer,
                name = "SecondaryContainer",
            ),
            ColorCell(
                color = MpeixTheme.palette.content,
                name = "Content",
            ),
            ColorCell(
                color = MpeixTheme.palette.contentAccent,
                name = "ContentAccent",
            ),
            ColorCell(
                color = MpeixTheme.palette.contentVariant,
                name = "ContentVariant",
            ),
            ColorCell(
                color = MpeixTheme.palette.contentDisabled,
                name = "ContentDisabled",
            ),
            ColorCell(
                color = MpeixTheme.palette.outline,
                name = "Outline",
            ),
            ColorCell(
                color = MpeixTheme.palette.classesTypeLecture,
                name = "Lecture",
            ),
            ColorCell(
                color = MpeixTheme.palette.classesTypePractice,
                name = "Practice",
            ),
            ColorCell(
                color = MpeixTheme.palette.classesTypeLab,
                name = "Lab",
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
            style = MpeixTheme.typography.paragraphBig,
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
