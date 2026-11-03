package kekmech.ru.mpeiapp.ui.main.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue", "MagicNumber")
internal val NavigationIcons.ModeHeatFilled: ImageVector
  get() {
    if (_ModeHeatFilled != null) {
      return _ModeHeatFilled!!
    }
    _ModeHeatFilled =
      ImageVector.Builder(
          name = "ModeHeatFilled",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(4f, 14f)
            quadTo(4f, 11.18f, 5.68f, 8.57f)
            quadToRelative(1.67f, -2.6f, 4.6f, -4.55f)
            quadTo(10.83f, 3.65f, 11.41f, 3.99f)
            reflectiveQuadTo(12f, 5f)
            verticalLineTo(6.3f)
            quadToRelative(0f, 0.85f, 0.59f, 1.42f)
            reflectiveQuadTo(14.03f, 8.3f)
            quadToRelative(0.43f, 0f, 0.81f, -0.19f)
            quadTo(15.23f, 7.93f, 15.53f, 7.57f)
            quadToRelative(0.2f, -0.25f, 0.51f, -0.31f)
            reflectiveQuadTo(16.63f, 7.4f)
            quadToRelative(1.57f, 1.13f, 2.48f, 2.88f)
            reflectiveQuadTo(20f, 14f)
            quadToRelative(0f, 2.2f, -1.07f, 4.01f)
            reflectiveQuadTo(16.1f, 20.88f)
            quadToRelative(0.43f, -0.6f, 0.66f, -1.31f)
            reflectiveQuadTo(17f, 18.05f)
            quadToRelative(0f, -1f, -0.38f, -1.89f)
            reflectiveQuadTo(15.55f, 14.58f)
            lineTo(12f, 11.1f)
            lineTo(8.48f, 14.58f)
            quadToRelative(-0.72f, 0.72f, -1.1f, 1.6f)
            reflectiveQuadTo(7f, 18.05f)
            quadToRelative(0f, 0.8f, 0.24f, 1.51f)
            reflectiveQuadTo(7.9f, 20.88f)
            quadTo(6.15f, 19.83f, 5.08f, 18.01f)
            reflectiveQuadTo(4f, 14f)
            close()
            moveToRelative(8f, -0.1f)
            lineToRelative(2.13f, 2.08f)
            quadToRelative(0.43f, 0.42f, 0.65f, 0.95f)
            reflectiveQuadTo(15f, 18.05f)
            quadToRelative(0f, 1.22f, -0.88f, 2.09f)
            reflectiveQuadTo(12f, 21f)
            reflectiveQuadTo(9.88f, 20.14f)
            reflectiveQuadTo(9f, 18.05f)
            quadTo(9f, 17.48f, 9.23f, 16.94f)
            reflectiveQuadTo(9.88f, 15.98f)
            lineTo(12f, 13.9f)
            close()
          }
        }
        .build()
    return _ModeHeatFilled!!
  }

private var _ModeHeatFilled: ImageVector? = null
