package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val Pencil: ImageVector
    get() {
        if (_Pencil != null) return _Pencil!!
        
        _Pencil = ImageVector.Builder(
            name = "Pencil",
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
                moveTo(35.222f, 33.598f)
                curveToRelative(-0.647f, -2.101f, -1.705f, -6.059f, -2.325f, -7.566f)
                curveToRelative(-0.501f, -1.216f, -0.969f, -2.438f, -1.544f, -3.014f)
                reflectiveCurveToRelative(-1.553f, -0.53f, -2.143f, 0.058f)
                curveToRelative(0f, 0f, -2.469f, 1.675f, -3.354f, 2.783f)
                curveToRelative(-1.108f, 0.882f, -2.785f, 3.357f, -2.785f, 3.357f)
                curveToRelative(-0.59f, 0.59f, -0.635f, 1.567f, -0.06f, 2.143f)
                curveToRelative(0.576f, 0.575f, 1.798f, 1.043f, 3.015f, 1.544f)
                curveToRelative(1.506f, 0.62f, 5.465f, 1.676f, 7.566f, 2.325f)
                curveToRelative(0.359f, 0.11f, 1.74f, -1.271f, 1.63f, -1.63f)
            }
            path(
                fill = SolidColor(Color(0xFFea596e))
            ) {
                moveTo(13.643f, 5.308f)
                arcToRelative(2.946f, 2.946f, 0f, false, true, 0f, 4.167f)
                lineToRelative(-4.167f, 4.168f)
                arcToRelative(2.95f, 2.95f, 0f, false, true, -4.167f, 0f)
                lineTo(1.141f, 9.475f)
                arcToRelative(2.95f, 2.95f, 0f, false, true, 0f, -4.167f)
                lineToRelative(4.167f, -4.167f)
                arcToRelative(2.946f, 2.946f, 0f, false, true, 4.167f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFffcc4d))
            ) {
                moveToRelative(31.353f, 23.018f)
                lineToRelative(-4.17f, 4.17f)
                lineToRelative(-4.163f, 4.165f)
                lineTo(7.392f, 15.726f)
                lineToRelative(8.335f, -8.334f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF292f33))
            ) {
                moveTo(32.078f, 34.763f)
                reflectiveCurveToRelative(2.709f, 1.489f, 3.441f, 0.757f)
                reflectiveCurveToRelative(-0.765f, -3.435f, -0.765f, -3.435f)
                reflectiveCurveToRelative(-2.566f, 0.048f, -2.676f, 2.678f)
            }
            path(
                fill = SolidColor(Color(0xFFccd6dd))
            ) {
                moveToRelative(2.183f, 10.517f)
                lineToRelative(8.335f, -8.335f)
                lineToRelative(5.208f, 5.209f)
                lineToRelative(-8.334f, 8.335f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF99aab5))
            ) {
                moveToRelative(3.225f, 11.558f)
                lineToRelative(8.334f, -8.334f)
                lineToRelative(1.042f, 1.042f)
                lineTo(4.267f, 12.6f)
                close()
                moveToRelative(2.083f, 2.086f)
                lineToRelative(8.335f, -8.335f)
                lineToRelative(1.042f, 1.042f)
                lineToRelative(-8.335f, 8.334f)
                close()
            }
        }.build()
        
        return _Pencil!!
    }

private var _Pencil: ImageVector? = null

