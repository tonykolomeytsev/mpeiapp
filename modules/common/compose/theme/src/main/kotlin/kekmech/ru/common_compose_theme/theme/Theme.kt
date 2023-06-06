package kekmech.ru.common_compose_theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kekmech.ru.common_compose_theme.color.DarkMpeixPalette
import kekmech.ru.common_compose_theme.color.LightMpeixPalette
import kekmech.ru.common_compose_theme.color.MpeixPalette
import kekmech.ru.common_compose_theme.typography.Typography

/**
 * Simple MpeiX palette mapped into Material3 palette
 */
private val LightColorScheme = lightColorScheme(
    primary = LightMpeixPalette.Primary,
    onPrimary = LightMpeixPalette.ContentAccent,
    primaryContainer = LightMpeixPalette.PrimaryContainer,
    onPrimaryContainer = LightMpeixPalette.Content,
    inversePrimary = LightMpeixPalette.Primary,
    secondary = LightMpeixPalette.Secondary,
    onSecondary = LightMpeixPalette.ContentAccent,
    secondaryContainer = LightMpeixPalette.SecondaryContainer,
    onSecondaryContainer = LightMpeixPalette.Content,
    tertiary = LightMpeixPalette.Tertiary,
    onTertiary = LightMpeixPalette.ContentAccent,
    tertiaryContainer = LightMpeixPalette.SecondaryContainer,
    onTertiaryContainer = LightMpeixPalette.Content,
    background = LightMpeixPalette.Background,
    onBackground = LightMpeixPalette.Content,
    surface = LightMpeixPalette.Surface,
    onSurface = LightMpeixPalette.Content,
    surfaceVariant = LightMpeixPalette.SurfacePlus3,
    onSurfaceVariant = LightMpeixPalette.Content,
    outline = LightMpeixPalette.Outline,
)

/**
 * Simple MpeiX palette mapped into Material3 palette
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkMpeixPalette.Primary,
    onPrimary = DarkMpeixPalette.ContentAccent,
    primaryContainer = DarkMpeixPalette.PrimaryContainer,
    onPrimaryContainer = DarkMpeixPalette.Content,
    inversePrimary = DarkMpeixPalette.Primary,
    secondary = DarkMpeixPalette.Secondary,
    onSecondary = DarkMpeixPalette.ContentAccent,
    secondaryContainer = DarkMpeixPalette.SecondaryContainer,
    onSecondaryContainer = DarkMpeixPalette.Content,
    tertiary = DarkMpeixPalette.Tertiary,
    onTertiary = DarkMpeixPalette.ContentAccent,
    tertiaryContainer = DarkMpeixPalette.SecondaryContainer,
    onTertiaryContainer = DarkMpeixPalette.Content,
    background = DarkMpeixPalette.Background,
    onBackground = DarkMpeixPalette.Content,
    surface = DarkMpeixPalette.Surface,
    onSurface = DarkMpeixPalette.Content,
    surfaceVariant = DarkMpeixPalette.SurfacePlus3,
    onSurfaceVariant = DarkMpeixPalette.Content,
    outline = DarkMpeixPalette.Outline,
)

@Composable
fun MpeixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !darkTheme,
        )
    }

    CompositionLocalProvider(
        LocalPalette provides if (darkTheme) DarkMpeixPalette else LightMpeixPalette,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

object MpeixTheme {

    val Palette: MpeixPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalPalette.current
}

internal val LocalPalette = staticCompositionLocalOf { LightMpeixPalette }
