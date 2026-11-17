package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Pushpin: ImageVector
    get() {
        if (_Pushpin != null) return _Pushpin!!
        
        _Pushpin = ImageVector.Builder(
            name = "Pushpin",
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
                fill = SolidColor(Color(0xFFbe1931))
            ) {
                moveTo(23.651f, 23.297f)
                lineTo(12.702f, 12.348f)
                lineToRelative(9.386f, -7.821f)
                lineToRelative(9.385f, 9.385f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFdd2e44))
            ) {
                moveTo(34.6f, 13.912f)
                arcToRelative(4.42f, 4.42f, 0f, false, true, -6.255f, 0f)
                lineToRelative(-6.257f, -6.256f)
                arcToRelative(4.426f, 4.426f, 0f, false, true, 6.257f, -6.258f)
                lineTo(34.6f, 7.656f)
                arcToRelative(4.423f, 4.423f, 0f, false, true, 0f, 6.256f)
            }
            path(
                fill = SolidColor(Color(0xFF99aab5))
            ) {
                moveTo(14f, 17.823f)
                reflectiveCurveTo(-0.593f, 35.029f, 0.188f, 35.813f)
                reflectiveCurveTo(18.177f, 22f, 18.177f, 22f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFdd2e44))
            ) {
                moveTo(25.215f, 27.991f)
                arcToRelative(4.423f, 4.423f, 0f, false, true, -6.258f, 0f)
                lineTo(8.009f, 17.041f)
                arcToRelative(4.425f, 4.425f, 0f, false, true, 6.258f, -6.256f)
                lineToRelative(10.948f, 10.949f)
                arcToRelative(4.424f, 4.424f, 0f, false, true, 0f, 6.257f)
            }
        }.build()
        
        return _Pushpin!!
    }

private var _Pushpin: ImageVector? = null

