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
internal val NavigationIcons.ModeHeat: ImageVector
  get() {
    if (_ModeHeat != null) {
      return _ModeHeat!!
    }
    _ModeHeat =
      ImageVector.Builder(
          name = "ModeHeat",
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
            moveTo(6.33f, 19.68f)
            quadTo(4f, 17.35f, 4f, 14f)
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
            quadToRelative(0f, 3.35f, -2.32f, 5.68f)
            reflectiveQuadTo(12f, 22f)
            reflectiveQuadTo(6.33f, 19.68f)
            close()
            moveTo(6f, 14f)
            quadToRelative(0f, 1.3f, 0.53f, 2.46f)
            reflectiveQuadToRelative(1.5f, 2.04f)
            quadTo(8f, 18.38f, 8f, 18.27f)
            quadToRelative(0f, -0.1f, 0f, -0.22f)
            quadToRelative(0f, -0.8f, 0.3f, -1.5f)
            reflectiveQuadTo(9.18f, 15.28f)
            lineTo(12f, 12.5f)
            lineToRelative(2.83f, 2.78f)
            quadToRelative(0.57f, 0.57f, 0.88f, 1.28f)
            reflectiveQuadToRelative(0.3f, 1.5f)
            quadToRelative(0f, 0.13f, 0f, 0.22f)
            quadToRelative(0f, 0.1f, -0.02f, 0.23f)
            quadToRelative(0.97f, -0.88f, 1.5f, -2.04f)
            reflectiveQuadTo(18f, 14f)
            quadToRelative(0f, -1.25f, -0.46f, -2.36f)
            reflectiveQuadTo(16.2f, 9.65f)
            quadToRelative(-0.5f, 0.33f, -1.05f, 0.49f)
            reflectiveQuadTo(14.03f, 10.3f)
            quadToRelative(-1.55f, 0f, -2.69f, -1.03f)
            reflectiveQuadTo(10.03f, 6.75f)
            quadTo(8.08f, 8.4f, 7.04f, 10.26f)
            reflectiveQuadTo(6f, 14f)
            close()
            moveToRelative(6f, 1.3f)
            lineToRelative(-1.42f, 1.4f)
            quadToRelative(-0.28f, 0.28f, -0.43f, 0.63f)
            quadTo(10f, 17.68f, 10f, 18.05f)
            quadToRelative(0f, 0.8f, 0.59f, 1.38f)
            reflectiveQuadTo(12f, 20f)
            reflectiveQuadToRelative(1.41f, -0.57f)
            reflectiveQuadTo(14f, 18.05f)
            quadToRelative(0f, -0.4f, -0.15f, -0.74f)
            reflectiveQuadTo(13.43f, 16.7f)
            lineTo(12f, 15.3f)
            close()
          }
        }
        .build()
    return _ModeHeat!!
  }

private var _ModeHeat: ImageVector? = null
