package com.composables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val TwemojiSparkles: ImageVector
    get() {
        if (_TwemojiSparkles != null) return _TwemojiSparkles!!
        
        _TwemojiSparkles = ImageVector.Builder(
            name = "TwemojiSparkles",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 36f,
            viewportHeight = 36f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFffac33))
            ) {
                moveToRelative(34.347f, 16.893f)
                lineToRelative(-8.899f, -3.294f)
                lineToRelative(-3.323f, -10.891f)
                arcToRelative(1f, 1f, 0f, false, false, -1.912f, 0f)
                lineToRelative(-3.322f, 10.891f)
                lineToRelative(-8.9f, 3.294f)
                arcToRelative(1f, 1f, 0f, false, false, 0f, 1.876f)
                lineToRelative(8.895f, 3.293f)
                lineToRelative(3.324f, 11.223f)
                arcToRelative(1f, 1f, 0f, false, false, 1.918f, -0.001f)
                lineToRelative(3.324f, -11.223f)
                lineToRelative(8.896f, -3.293f)
                arcToRelative(0.998f, 0.998f, 0f, false, false, -0.001f, -1.875f)
            }
            path(
                fill = SolidColor(Color(0xFFffcc4d))
            ) {
                moveToRelative(14.347f, 27.894f)
                lineToRelative(-2.314f, -0.856f)
                lineToRelative(-0.9f, -3.3f)
                arcToRelative(0.998f, 0.998f, 0f, false, false, -1.929f, -0.001f)
                lineToRelative(-0.9f, 3.3f)
                lineToRelative(-2.313f, 0.856f)
                arcToRelative(1f, 1f, 0f, false, false, 0f, 1.876f)
                lineToRelative(2.301f, 0.853f)
                lineToRelative(0.907f, 3.622f)
                arcToRelative(1f, 1f, 0f, false, false, 1.94f, -0.001f)
                lineToRelative(0.907f, -3.622f)
                lineToRelative(2.301f, -0.853f)
                arcToRelative(0.997f, 0.997f, 0f, false, false, 0f, -1.874f)
                moveTo(10.009f, 6.231f)
                lineToRelative(-2.364f, -0.875f)
                lineToRelative(-0.876f, -2.365f)
                arcToRelative(0.999f, 0.999f, 0f, false, false, -1.876f, 0f)
                lineToRelative(-0.875f, 2.365f)
                lineToRelative(-2.365f, 0.875f)
                arcToRelative(1f, 1f, 0f, false, false, 0f, 1.876f)
                lineToRelative(2.365f, 0.875f)
                lineToRelative(0.875f, 2.365f)
                arcToRelative(1f, 1f, 0f, false, false, 1.876f, 0f)
                lineToRelative(0.875f, -2.365f)
                lineToRelative(2.365f, -0.875f)
                arcToRelative(1f, 1f, 0f, false, false, 0f, -1.876f)
            }
        }.build()
        
        return _TwemojiSparkles!!
    }

private var _TwemojiSparkles: ImageVector? = null

