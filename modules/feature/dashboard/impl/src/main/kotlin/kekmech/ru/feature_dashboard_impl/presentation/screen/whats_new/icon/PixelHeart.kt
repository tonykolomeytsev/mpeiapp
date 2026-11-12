package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pixel art of the heart, one character per pixel:
 * `O` outline, `R` fill, `W` highlight, `D` shade, `.` transparent
 */
private val PixelHeartRows = listOf(
    "..OOO...OOO..",
    ".ORRRO.ORRRO.",
    "ORWWRRORRRRRO",
    "ORWRRRRRRRRRO",
    "ORRRRRRRRRRRO",
    "ORRRRRRRRRRDO",
    ".ORRRRRRRRDO.",
    "..ORRRRRRDO..",
    "...ORRRRDO...",
    "....ORRDO....",
    ".....ORO.....",
    "......O......",
)

private const val PixelSizeDp = 4

/**
 * Every layer covers the pixels of the next ones, so antialiasing does not leave seams between colors
 */
private val PixelHeartLayers = listOf(
    Color(0xFF7A0C1B) to "ORWD",
    Color(0xFFE53950) to "RWD",
    Color(0xFFFFFFFF) to "W",
    Color(0xFFB0213A) to "D",
)

internal val PixelHeart: ImageVector by lazy {
    val width = PixelHeartRows.first().length
    val height = PixelHeartRows.size
    ImageVector.Builder(
        name = "PixelHeart",
        defaultWidth = (width * PixelSizeDp).dp,
        defaultHeight = (height * PixelSizeDp).dp,
        viewportWidth = width.toFloat(),
        viewportHeight = height.toFloat(),
    ).apply {
        PixelHeartLayers.forEach { (color, pixels) ->
            path(fill = SolidColor(color)) {
                PixelHeartRows.forEachIndexed { y, row ->
                    row.forEachIndexed { x, pixel ->
                        if (pixel in pixels) {
                            moveTo(x.toFloat(), y.toFloat())
                            horizontalLineToRelative(1f)
                            verticalLineToRelative(1f)
                            horizontalLineToRelative(-1f)
                            close()
                        }
                    }
                }
            }
        }
    }.build()
}
