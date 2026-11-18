package kekmech.ru.feature_schedule_ui.compose.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val SoccerBall: ImageVector
    get() {
        if (_SoccerBall != null) return _SoccerBall!!
        
        _SoccerBall = ImageVector.Builder(
            name = "SoccerBall",
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
                fill = SolidColor(Color(0xFFf5f8fa))
            ) {
                moveTo(36f, 18f)
                arcTo(18f, 18f, 0f, false, true, 18f, 36f)
                arcTo(18f, 18f, 0f, false, true, 0f, 18f)
                arcTo(18f, 18f, 0f, false, true, 36f, 18f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFccd6dd))
            ) {
                moveTo(18f, 11f)
                arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
                verticalLineTo(3f)
                arcToRelative(1f, 1f, 0f, false, true, 2f, 0f)
                verticalLineToRelative(7f)
                arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                moveToRelative(-6.583f, 4.5f)
                arcToRelative(1f, 1f, 0f, false, true, -0.302f, -0.047f)
                lineToRelative(-8.041f, -2.542f)
                arcToRelative(1f, 1f, 0f, true, true, 0.603f, -1.907f)
                lineToRelative(8.042f, 2.542f)
                arcToRelative(1f, 1f, 0f, false, true, -0.302f, 1.954f)
                moveToRelative(13.625f, -0.291f)
                arcToRelative(1.001f, 1.001f, 0f, false, true, -0.278f, -1.961f)
                lineToRelative(6.75f, -1.958f)
                arcToRelative(1f, 1f, 0f, false, true, 0.556f, 1.921f)
                lineToRelative(-6.75f, 1.958f)
                arcToRelative(1f, 1f, 0f, false, true, -0.278f, 0.04f)
                moveToRelative(2.001f, 14.958f)
                arcToRelative(1f, 1f, 0f, false, true, -0.803f, -0.403f)
                lineToRelative(-5.459f, -7.333f)
                arcToRelative(1f, 1f, 0f, true, true, 1.604f, -1.194f)
                lineToRelative(5.459f, 7.333f)
                arcToRelative(1f, 1f, 0f, false, true, -0.801f, 1.597f)
                moveToRelative(-18.294f, -0.083f)
                arcToRelative(1f, 1f, 0f, false, true, -0.82f, -1.571f)
                lineToRelative(5.125f, -7.375f)
                arcToRelative(1f, 1f, 0f, true, true, 1.642f, 1.141f)
                lineToRelative(-5.125f, 7.375f)
                arcToRelative(1f, 1f, 0f, false, true, -0.822f, 0.43f)
                moveTo(3.5f, 27.062f)
                curveToRelative(-0.44f, 0f, -0.844f, -0.293f, -0.965f, -0.738f)
                lineTo(0.347f, 18.262f)
                arcToRelative(1f, 1f, 0f, true, true, 1.931f, -0.523f)
                lineToRelative(2.188f, 8.062f)
                arcToRelative(0.998f, 0.998f, 0f, false, true, -0.966f, 1.261f)
                moveTo(22f, 34f)
                horizontalLineToRelative(-9f)
                arcToRelative(1f, 1f, 0f, true, true, 0f, -2f)
                horizontalLineToRelative(9f)
                arcToRelative(1f, 1f, 0f, true, true, 0f, 2f)
                moveToRelative(10.126f, -6.875f)
                arcToRelative(1f, 1f, 0f, false, true, -0.971f, -1.239f)
                lineToRelative(2.125f, -8.625f)
                arcToRelative(1f, 1f, 0f, true, true, 1.941f, 0.479f)
                lineToRelative(-2.125f, 8.625f)
                curveToRelative(-0.113f, 0.455f, -0.521f, 0.76f, -0.97f, 0.76f)
                moveTo(30.312f, 7.688f)
                arcToRelative(1f, 1f, 0f, false, true, -0.5f, -0.134f)
                lineTo(22.25f, 3.179f)
                arcToRelative(1f, 1f, 0f, false, true, -0.364f, -1.367f)
                arcToRelative(1f, 1f, 0f, false, true, 1.366f, -0.365f)
                lineToRelative(7.562f, 4.375f)
                arcToRelative(1f, 1f, 0f, false, true, -0.502f, 1.866f)
                moveToRelative(-24.811f, 0f)
                arcToRelative(1f, 1f, 0f, false, true, -0.584f, -1.813f)
                lineToRelative(6.188f, -4.438f)
                arcToRelative(1.001f, 1.001f, 0f, false, true, 1.167f, 1.625f)
                lineTo(6.083f, 7.5f)
                arcToRelative(1f, 1f, 0f, false, true, -0.582f, 0.188f)
            }
            path(
                fill = SolidColor(Color(0xFF31373d))
            ) {
                moveToRelative(25.493f, 13.516f)
                lineToRelative(-7.208f, -5.083f)
                arcToRelative(1f, 1f, 0f, false, false, -1.161f, 0.006f)
                lineToRelative(-7.167f, 5.167f)
                arcToRelative(1f, 1f, 0f, false, false, -0.375f, 1.091f)
                lineToRelative(2.5f, 8.583f)
                arcToRelative(1f, 1f, 0f, false, false, 0.96f, 0.72f)
                horizontalLineTo(22f)
                arcToRelative(1f, 1f, 0f, false, false, 0.948f, -0.681f)
                lineToRelative(2.917f, -8.667f)
                arcToRelative(1f, 1f, 0f, false, false, -0.372f, -1.136f)
                moveTo(1.292f, 19.542f)
                arcToRelative(1f, 1f, 0f, false, false, 0.872f, -0.51f)
                lineToRelative(3.375f, -6f)
                arcToRelative(1f, 1f, 0f, false, false, 0.108f, -0.291f)
                lineTo(6.98f, 6.2f)
                arcTo(1f, 1f, 0f, false, false, 6f, 5f)
                horizontalLineToRelative(-0.428f)
                curveTo(2.145f, 8.277f, 0f, 12.884f, 0f, 18f)
                curveToRelative(0f, 0.266f, 0.028f, 0.525f, 0.04f, 0.788f)
                lineToRelative(0.602f, 0.514f)
                curveToRelative(0.182f, 0.156f, 0.413f, 0.24f, 0.65f, 0.24f)
                moveToRelative(9.325f, -16.547f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, 0.553f, 0.412f)
                lineToRelative(6.375f, 1.042f)
                arcToRelative(0.8f, 0.8f, 0f, false, false, 0.243f, 0f)
                lineToRelative(6.084f, -1f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, 0.613f, -0.889f)
                lineToRelative(-0.292f, -1.443f)
                arcTo(17.9f, 17.9f, 0f, false, false, 18f, 0f)
                curveToRelative(-2.425f, 0f, -4.734f, 0.486f, -6.845f, 1.356f)
                lineToRelative(-0.521f, 0.95f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, -0.017f, 0.689f)
                moveToRelative(20.517f, 2.724f)
                lineToRelative(-1.504f, -0.095f)
                arcToRelative(0.76f, 0.76f, 0f, false, false, -0.609f, 0.249f)
                arcToRelative(0.74f, 0.74f, 0f, false, false, -0.175f, 0.63f)
                lineToRelative(1.167f, 6.198f)
                arcToRelative(0.7f, 0.7f, 0f, false, false, 0.093f, 0.224f)
                curveToRelative(1.492f, 2.504f, 3.152f, 5.301f, 3.381f, 5.782f)
                curveToRelative(0.024f, 0.084f, 0.062f, 0.079f, 0.114f, 0.151f)
                curveToRelative(0.14f, 0.195f, 0.372f, 0.142f, 0.612f, 0.142f)
                horizontalLineToRelative(0.007f)
                curveToRelative(0.198f, 0f, 0.323f, 0.094f, 1.768f, -0.753f)
                curveToRelative(0.001f, -0.083f, 0.012f, -0.164f, 0.012f, -0.247f)
                curveToRelative(0f, -4.753f, -1.856f, -9.064f, -4.866f, -12.281f)
                moveTo(14.541f, 33.376f)
                arcToRelative(0.76f, 0.76f, 0f, false, false, -0.191f, -0.544f)
                lineToRelative(-4.5f, -5f)
                arcToRelative(0.8f, 0.8f, 0f, false, false, -0.211f, -0.163f)
                curveToRelative(-5.885f, -3.069f, -5.994f, -3.105f, -6.066f, -3.13f)
                arcToRelative(0.8f, 0.8f, 0f, false, false, -0.242f, -0.039f)
                curveToRelative(-0.537f, 0f, -0.695f, 0.065f, -1.185f, 2.024f)
                arcToRelative(18.04f, 18.04f, 0f, false, false, 10.644f, 8.703f)
                lineToRelative(1.5f, -1.333f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, 0.251f, -0.518f)
                moveToRelative(17.833f, -8.567f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, -0.592f, 0.005f)
                lineToRelative(-6.083f, 2.667f)
                arcToRelative(0.74f, 0.74f, 0f, false, false, -0.274f, 0.205f)
                lineToRelative(-4.25f, 5.083f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, 0.1f, 1.062f)
                curveToRelative(0.683f, 0.559f, 1.261f, 1.03f, 1.767f, 1.44f)
                arcToRelative(18.02f, 18.02f, 0f, false, false, 10.454f, -8.146f)
                lineToRelative(-0.712f, -1.889f)
                arcToRelative(0.75f, 0.75f, 0f, false, false, -0.41f, -0.427f)
            }
        }.build()
        
        return _SoccerBall!!
    }

private var _SoccerBall: ImageVector? = null

