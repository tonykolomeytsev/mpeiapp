package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Eggplant: ImageVector
    get() {
        if (_Eggplant != null) return _Eggplant!!
        
        _Eggplant = ImageVector.Builder(
            name = "Eggplant",
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
                fill = SolidColor(Color(0xFF744eaa))
            ) {
                moveTo(6f, 4f)
                curveToRelative(3f, 0f, 5f, 2f, 8f, 6f)
                reflectiveCurveToRelative(7.957f, 7.191f, 12f, 8f)
                curveToRelative(5f, 1f, 9f, 5f, 9f, 11f)
                curveToRelative(0f, 4.897f, -3.846f, 7f, -9f, 7f)
                curveToRelative(-5f, 0f, -9f, -3f, -14f, -8f)
                reflectiveCurveTo(2f, 14f, 2f, 10f)
                reflectiveCurveToRelative(1f, -6f, 4f, -6f)
            }
            path(
                fill = SolidColor(Color(0xFF77b255))
            ) {
                moveTo(3.515f, 0f)
                curveToRelative(1.248f, 0f, 1.248f, 1.248f, 1.248f, 2.495f)
                curveToRelative(0f, 1.764f, 1.248f, 1.129f, 2.496f, 1.129f)
                curveTo(8.505f, 3.624f, 11f, 6f, 11f, 6f)
                horizontalLineTo(7.258f)
                curveToRelative(-1.248f, 0f, 0f, 2.614f, -1.248f, 2.614f)
                reflectiveCurveTo(4.762f, 7.426f, 3.515f, 7.426f)
                reflectiveCurveTo(2f, 11f, 2f, 11f)
                reflectiveCurveToRelative(-1.604f, -4.153f, 0.267f, -6.024f)
                curveTo(3.515f, 3.728f, 1.02f, 0f, 3.515f, 0f)
            }
        }.build()
        
        return _Eggplant!!
    }

private var _Eggplant: ImageVector? = null

