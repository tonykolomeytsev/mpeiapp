package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val StraightRuler: ImageVector
    get() {
        if (_StraightRuler != null) return _StraightRuler!!
        
        _StraightRuler = ImageVector.Builder(
            name = "StraightRuler",
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
                fill = SolidColor(Color(0xFFffcc4d))
            ) {
                moveTo(30.021f, 34.264f)
                arcToRelative(4f, 4f, 0f, false, true, -5.656f, 0f)
                lineTo(1.736f, 11.636f)
                arcToRelative(4f, 4f, 0f, false, true, 0f, -5.657f)
                lineToRelative(4.243f, -4.242f)
                arcToRelative(4f, 4f, 0f, false, true, 5.657f, 0f)
                lineToRelative(22.626f, 22.628f)
                arcToRelative(4f, 4f, 0f, false, true, 0.001f, 5.656f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF292f33))
            ) {
                moveTo(9.515f, 6.687f)
                arcToRelative(1f, 1f, 0f, false, true, -1.414f, -1.415f)
                lineToRelative(3.536f, -3.536f)
                lineToRelative(1.414f, 1.415f)
                close()
                moveToRelative(5.656f, 1.414f)
                arcToRelative(0.999f, 0.999f, 0f, true, true, -1.414f, -1.414f)
                lineToRelative(1.415f, -1.415f)
                lineToRelative(1.414f, 1.415f)
                close()
                moveToRelative(1.415f, 5.656f)
                arcToRelative(0.999f, 0.999f, 0f, true, true, -1.414f, -1.414f)
                lineToRelative(3.536f, -3.536f)
                curveToRelative(0.486f, 0.486f, 0.929f, 0.928f, 1.414f, 1.415f)
                close()
                moveToRelative(5.656f, 1.414f)
                arcToRelative(1f, 1f, 0f, false, true, -1.413f, 0f)
                arcToRelative(1f, 1f, 0f, false, true, 0f, -1.414f)
                lineToRelative(1.414f, -1.415f)
                curveToRelative(0.485f, 0.487f, 0.928f, 0.928f, 1.414f, 1.415f)
                close()
                moveToRelative(1.415f, 5.657f)
                arcToRelative(1f, 1f, 0f, false, true, -1.415f, -1.414f)
                lineToRelative(3.536f, -3.535f)
                lineToRelative(1.414f, 1.414f)
                close()
                moveToRelative(7.071f, 7.071f)
                arcToRelative(1f, 1f, 0f, false, true, -1.413f, 0f)
                arcToRelative(1f, 1f, 0f, false, true, -0.001f, -1.414f)
                lineToRelative(3.536f, -3.535f)
                lineToRelative(1.414f, 1.414f)
                close()
                moveToRelative(-1.415f, -5.657f)
                arcToRelative(1f, 1f, 0f, true, true, -1.414f, -1.413f)
                lineToRelative(1.415f, -1.414f)
                lineToRelative(1.413f, 1.413f)
                close()
            }
        }.build()
        
        return _StraightRuler!!
    }

private var _StraightRuler: ImageVector? = null

