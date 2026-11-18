package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val ArtistPalette: ImageVector
    get() {
        if (_ArtistPalette != null) return _ArtistPalette!!
        
        _ArtistPalette = ImageVector.Builder(
            name = "ArtistPalette",
            defaultWidth = 36.dp,
            defaultHeight = 36.dp,
            viewportWidth = 36f,
            viewportHeight = 36f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
            }
            path(
                fill = SolidColor(Color(0xFFd99e82))
            ) {
                moveTo(18f, 3.143f)
                curveToRelative(-9.941f, 0f, -18f, 6.908f, -18f, 15.428f)
                curveToRelative(0f, 1.066f, 0.126f, 2.107f, 0.367f, 3.112f)
                curveTo(2.146f, 24.744f, 3.377f, 22.812f, 9f, 20f)
                curveToRelative(5.727f, -2.864f, 0f, 4f, -2f, 8f)
                curveToRelative(-0.615f, 1.23f, -0.282f, 2.271f, 0.56f, 3.124f)
                curveTo(10.506f, 32.928f, 14.104f, 34f, 18f, 34f)
                curveToRelative(9.941f, 0f, 18f, -6.907f, 18f, -15.429f)
                curveToRelative(0f, -8.52f, -8.059f, -15.428f, -18f, -15.428f)
                moveToRelative(2.849f, 24.447f)
                curveToRelative(-0.395f, 1.346f, -2.46f, 1.924f, -4.613f, 1.291f)
                curveToRelative(-2.153f, -0.632f, -3.578f, -2.234f, -3.183f, -3.581f)
                reflectiveCurveToRelative(2.46f, -1.924f, 4.613f, -1.29f)
                curveToRelative(2.153f, 0.631f, 3.578f, 2.233f, 3.183f, 3.58f)
            }
            path(
                fill = SolidColor(Color(0xFF5c913b))
            ) {
                moveTo(13f, 11f)
                arcTo(3f, 3f, 0f, false, true, 10f, 14f)
                arcTo(3f, 3f, 0f, false, true, 7f, 11f)
                arcTo(3f, 3f, 0f, false, true, 13f, 11f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF269))
            ) {
                moveTo(23f, 9f)
                arcTo(3f, 3f, 0f, false, true, 20f, 12f)
                arcTo(3f, 3f, 0f, false, true, 17f, 9f)
                arcTo(3f, 3f, 0f, false, true, 23f, 9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFdd2e44))
            ) {
                moveTo(32f, 15f)
                arcTo(3f, 3f, 0f, false, true, 29f, 18f)
                arcTo(3f, 3f, 0f, false, true, 26f, 15f)
                arcTo(3f, 3f, 0f, false, true, 32f, 15f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFffcc4d))
            ) {
                moveTo(31f, 24f)
                arcTo(3f, 3f, 0f, false, true, 28f, 27f)
                arcTo(3f, 3f, 0f, false, true, 25f, 24f)
                arcTo(3f, 3f, 0f, false, true, 31f, 24f)
                close()
            }
        }.build()
        
        return _ArtistPalette!!
    }

private var _ArtistPalette: ImageVector? = null

