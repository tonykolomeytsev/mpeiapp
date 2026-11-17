package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Bookmark: ImageVector
    get() {
        if (_Bookmark != null) return _Bookmark!!
        
        _Bookmark = ImageVector.Builder(
            name = "Bookmark",
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
                fill = SolidColor(Color(0xFFaab8c2))
            ) {
                moveTo(35f, 26f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, 4f)
                horizontalLineTo(5f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, -4f)
                verticalLineTo(6.313f)
                curveTo(1f, 4.104f, 6.791f, 0f, 9f, 0f)
                horizontalLineToRelative(20.625f)
                curveTo(32.719f, 0f, 35f, 2.312f, 35f, 5.375f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFf5f8fa))
            ) {
                moveTo(33f, 30f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, 4f)
                horizontalLineTo(7f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, -4f)
                verticalLineTo(6f)
                curveToRelative(0f, -4.119f, -0.021f, -4f, 5f, -4f)
                horizontalLineToRelative(21f)
                arcToRelative(4f, 4f, 0f, false, true, 4f, 4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFfff))
            ) {
                moveTo(31f, 31f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, 3f)
                horizontalLineTo(4f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, -3f)
                verticalLineTo(7f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, -3f)
                horizontalLineToRelative(24f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, 3f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFaab8c2))
            ) {
                moveTo(31f, 32f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, 4f)
                horizontalLineTo(6f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, -4f)
                verticalLineTo(10f)
                arcToRelative(4f, 4f, 0f, false, true, 4f, -4f)
                horizontalLineToRelative(21f)
                arcToRelative(4f, 4f, 0f, false, true, 4f, 4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFe1e8ed))
            ) {
                moveTo(29f, 32f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, 4f)
                horizontalLineTo(6f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, -4f)
                verticalLineTo(12f)
                arcToRelative(4f, 4f, 0f, false, true, 4f, -4f)
                horizontalLineToRelative(19.335f)
                curveTo(27.544f, 8f, 29f, 9.456f, 29f, 11.665f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFaab8c2))
            ) {
                moveTo(6f, 6f)
                curveTo(4.312f, 6f, 4.269f, 4.078f, 5f, 3.25f)
                curveTo(5.832f, 2.309f, 7.125f, 2f, 9.438f, 2f)
                horizontalLineTo(11f)
                verticalLineTo(0f)
                horizontalLineTo(8.281f)
                curveTo(4.312f, 0f, 1f, 2.5f, 1f, 5.375f)
                verticalLineTo(32f)
                arcToRelative(4f, 4f, 0f, false, false, 4f, 4f)
                horizontalLineToRelative(2f)
                verticalLineTo(6f)
                close()
            }
            group {
                path(
                    fill = SolidColor(Color(0xFFdd2e44))
                ) {
                    moveTo(17f, 4f)
                    verticalLineToRelative(23f)
                    lineToRelative(4f, -6f)
                    lineToRelative(4f, 6f)
                    verticalLineTo(4f)
                    close()
                }
            }
            group {
                path(
                    fill = SolidColor(Color(0xFFdd2e44))
                ) {
                    moveTo(25f, 28f)
                    arcToRelative(1f, 1f, 0f, false, true, -0.832f, -0.445f)
                    lineTo(21f, 22.803f)
                    lineToRelative(-3.168f, 4.752f)
                    arcTo(0.998f, 0.998f, 0f, false, true, 16f, 27f)
                    verticalLineTo(4f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(8f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(23f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                    moveToRelative(-4f, -8f)
                    curveToRelative(0.334f, 0f, 0.646f, 0.167f, 0.832f, 0.445f)
                    lineTo(24f, 23.697f)
                    verticalLineTo(5f)
                    horizontalLineToRelative(-6f)
                    verticalLineToRelative(18.697f)
                    lineToRelative(2.168f, -3.252f)
                    curveToRelative(0.186f, -0.278f, 0.498f, -0.445f, 0.832f, -0.445f)
                }
            }
            path(
                fill = SolidColor(Color(0xFFf5f8fa))
            ) {
                moveTo(15f, 2f)
                horizontalLineToRelative(12f)
                verticalLineToRelative(2f)
                horizontalLineTo(15f)
                close()
            }
        }.build()
        
        return _Bookmark!!
    }

private var _Bookmark: ImageVector? = null

