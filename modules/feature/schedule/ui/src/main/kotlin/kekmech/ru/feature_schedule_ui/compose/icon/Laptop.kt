package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Laptop: ImageVector
    get() {
        if (_Laptop != null) return _Laptop!!
        
        _Laptop = ImageVector.Builder(
            name = "Laptop",
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
                fill = SolidColor(Color(0xFFccd6dd))
            ) {
                moveTo(34f, 29.096f)
                curveToRelative(-0.417f, -0.963f, -0.896f, -2.008f, -2f, -2.008f)
                horizontalLineToRelative(-1f)
                curveToRelative(1.104f, 0f, 2f, -0.899f, 2f, -2.008f)
                verticalLineTo(8.008f)
                arcTo(2.004f, 2.004f, 0f, false, false, 31f, 6f)
                horizontalLineTo(5f)
                curveToRelative(-1.104f, 0f, -2f, 0.899f, -2f, 2.008f)
                verticalLineTo(25.08f)
                curveToRelative(0f, 1.109f, 0.896f, 2.008f, 2f, 2.008f)
                horizontalLineTo(4f)
                curveToRelative(-1.104f, 0f, -1.667f, 1.004f, -2f, 2.008f)
                lineToRelative(-2f, 4.895f)
                curveTo(0f, 35.101f, 0.896f, 36f, 2f, 36f)
                horizontalLineToRelative(32f)
                curveToRelative(1.104f, 0f, 2f, -0.899f, 2f, -2.008f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9aaab4))
            ) {
                moveToRelative(0.008f, 34.075f)
                lineToRelative(0.006f, 0.057f)
                lineToRelative(0.17f, 0.692f)
                arcTo(2f, 2f, 0f, false, false, 2f, 36f)
                horizontalLineToRelative(32f)
                arcToRelative(2f, 2f, 0f, false, false, 1.992f, -1.925f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF5dadec))
            ) {
                moveTo(31f, 24.075f)
                curveToRelative(0f, 0.555f, -0.447f, 1.004f, -1f, 1.004f)
                horizontalLineTo(6f)
                curveToRelative(-0.552f, 0f, -1f, -0.449f, -1f, -1.004f)
                verticalLineTo(9.013f)
                curveToRelative(0f, -0.555f, 0.448f, -1.004f, 1f, -1.004f)
                horizontalLineToRelative(24f)
                curveToRelative(0.553f, 0f, 1f, 0.45f, 1f, 1.004f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFaebbc1))
            ) {
                moveToRelative(32.906f, 31.042f)
                lineToRelative(-0.76f, -2.175f)
                curveToRelative(-0.239f, -0.46f, -0.635f, -0.837f, -1.188f, -0.837f)
                horizontalLineTo(5.11f)
                curveToRelative(-0.552f, 0f, -0.906f, 0.408f, -1.156f, 1.036f)
                lineToRelative(-0.688f, 1.977f)
                curveToRelative(-0.219f, 0.596f, 0.448f, 1.004f, 1f, 1.004f)
                horizontalLineToRelative(7.578f)
                reflectiveCurveToRelative(0.937f, -0.047f, 1.103f, -0.608f)
                curveToRelative(0.192f, -0.648f, 0.415f, -1.624f, 0.463f, -1.796f)
                curveToRelative(0.074f, -0.264f, 0.388f, -0.531f, 0.856f, -0.531f)
                horizontalLineToRelative(8.578f)
                curveToRelative(0.5f, 0f, 0.746f, 0.253f, 0.811f, 0.566f)
                curveToRelative(0.042f, 0.204f, 0.312f, 1.141f, 0.438f, 1.782f)
                curveToRelative(0.111f, 0.571f, 1.221f, 0.586f, 1.221f, 0.586f)
                horizontalLineToRelative(6.594f)
                curveToRelative(0.551f, 0f, 1.217f, -0.471f, 0.998f, -1.004f)
            }
            path(
                fill = SolidColor(Color(0xFF9aaab4))
            ) {
                moveTo(22.375f, 33.113f)
                horizontalLineToRelative(-7.781f)
                curveToRelative(-0.375f, 0f, -0.538f, -0.343f, -0.484f, -0.675f)
                curveToRelative(0.054f, -0.331f, 0.359f, -1.793f, 0.383f, -1.963f)
                curveToRelative(0.023f, -0.171f, 0.274f, -0.375f, 0.524f, -0.375f)
                horizontalLineToRelative(7.015f)
                curveToRelative(0.297f, 0f, 0.49f, 0.163f, 0.55f, 0.489f)
                curveToRelative(0.059f, 0.327f, 0.302f, 1.641f, 0.321f, 1.941f)
                reflectiveCurveToRelative(-0.169f, 0.583f, -0.528f, 0.583f)
            }
        }.build()
        
        return _Laptop!!
    }

private var _Laptop: ImageVector? = null

