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
internal val NavigationIcons.LocationOn: ImageVector
  get() {
    if (_LocationOn != null) {
      return _LocationOn!!
    }
    _LocationOn =
      ImageVector.Builder(
          name = "LocationOn",
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
            moveTo(12f, 19.35f)
            quadToRelative(3.05f, -2.8f, 4.53f, -5.09f)
            quadTo(18f, 11.98f, 18f, 10.2f)
            quadTo(18f, 7.47f, 16.26f, 5.74f)
            quadTo(14.53f, 4f, 12f, 4f)
            reflectiveQuadTo(7.74f, 5.74f)
            quadTo(6f, 7.47f, 6f, 10.2f)
            quadToRelative(0f, 1.78f, 1.48f, 4.06f)
            reflectiveQuadTo(12f, 19.35f)
            close()
            moveTo(11.3f, 21.2f)
            quadTo(10.95f, 21.08f, 10.68f, 20.83f)
            quadTo(9.05f, 19.33f, 7.8f, 17.9f)
            quadTo(6.55f, 16.48f, 5.71f, 15.14f)
            reflectiveQuadTo(4.44f, 12.56f)
            reflectiveQuadTo(4f, 10.2f)
            quadTo(4f, 6.45f, 6.41f, 4.22f)
            reflectiveQuadTo(12f, 2f)
            reflectiveQuadToRelative(5.59f, 2.22f)
            reflectiveQuadTo(20f, 10.2f)
            quadToRelative(0f, 1.13f, -0.44f, 2.36f)
            reflectiveQuadToRelative(-1.27f, 2.57f)
            reflectiveQuadTo(16.2f, 17.9f)
            quadToRelative(-1.25f, 1.43f, -2.87f, 2.93f)
            quadTo(13.05f, 21.08f, 12.7f, 21.2f)
            reflectiveQuadTo(12f, 21.33f)
            reflectiveQuadTo(11.3f, 21.2f)
            close()
            moveTo(12f, 10f)
            close()
            moveToRelative(1.41f, 1.41f)
            quadTo(14f, 10.83f, 14f, 10f)
            quadTo(14f, 9.17f, 13.41f, 8.59f)
            reflectiveQuadTo(12f, 8f)
            reflectiveQuadTo(10.59f, 8.59f)
            reflectiveQuadTo(10f, 10f)
            reflectiveQuadToRelative(0.59f, 1.41f)
            reflectiveQuadTo(12f, 12f)
            reflectiveQuadToRelative(1.41f, -0.59f)
            close()
          }
        }
        .build()
    return _LocationOn!!
  }

private var _LocationOn: ImageVector? = null
