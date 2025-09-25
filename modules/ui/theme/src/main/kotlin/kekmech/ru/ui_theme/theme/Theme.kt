package kekmech.ru.ui_theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kekmech.ru.ui_theme.color.DarkMpeixPalette
import kekmech.ru.ui_theme.color.LightMpeixPalette
import kekmech.ru.ui_theme.color.MpeixPalette
import kekmech.ru.ui_theme.typography.MpeixTypography

/**
 * Simple MpeiX palette mapped into Material3 palette
 */
private val LightColorScheme = lightColorScheme(
    primary = LightMpeixPalette.primary,
    onPrimary = LightMpeixPalette.contentAccent,
    primaryContainer = LightMpeixPalette.primaryContainer,
    onPrimaryContainer = LightMpeixPalette.content,
    inversePrimary = LightMpeixPalette.primary,
    secondary = LightMpeixPalette.secondary,
    onSecondary = LightMpeixPalette.contentAccent,
    secondaryContainer = LightMpeixPalette.secondaryContainer,
    onSecondaryContainer = LightMpeixPalette.content,
    tertiary = LightMpeixPalette.tertiary,
    onTertiary = LightMpeixPalette.contentAccent,
    tertiaryContainer = LightMpeixPalette.secondaryContainer,
    onTertiaryContainer = LightMpeixPalette.content,
    background = LightMpeixPalette.background,
    onBackground = LightMpeixPalette.content,
    surface = LightMpeixPalette.surface,
    onSurface = LightMpeixPalette.content,
    surfaceVariant = LightMpeixPalette.surfacePlus3,
    onSurfaceVariant = LightMpeixPalette.content,
    outline = LightMpeixPalette.outline,
    outlineVariant = LightMpeixPalette.outline.copy(alpha = 0.5f),
)

/**
 * Simple MpeiX palette mapped into Material3 palette
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkMpeixPalette.primary,
    onPrimary = DarkMpeixPalette.contentAccent,
    primaryContainer = DarkMpeixPalette.primaryContainer,
    onPrimaryContainer = DarkMpeixPalette.content,
    inversePrimary = DarkMpeixPalette.primary,
    secondary = DarkMpeixPalette.secondary,
    onSecondary = DarkMpeixPalette.contentAccent,
    secondaryContainer = DarkMpeixPalette.secondaryContainer,
    onSecondaryContainer = DarkMpeixPalette.content,
    tertiary = DarkMpeixPalette.tertiary,
    onTertiary = DarkMpeixPalette.contentAccent,
    tertiaryContainer = DarkMpeixPalette.secondaryContainer,
    onTertiaryContainer = DarkMpeixPalette.content,
    background = DarkMpeixPalette.background,
    onBackground = DarkMpeixPalette.content,
    surface = DarkMpeixPalette.surface,
    onSurface = DarkMpeixPalette.content,
    surfaceVariant = DarkMpeixPalette.surfacePlus3,
    onSurfaceVariant = DarkMpeixPalette.content,
    outline = DarkMpeixPalette.outline,
    outlineVariant = DarkMpeixPalette.outline.copy(alpha = 0.5f),
)

@Composable
public fun MpeixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = remember { MpeixTypography() }
    val isInPreview = LocalInspectionMode.current

    if (!isInPreview) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme,
            )
        }
    }

    CompositionLocalProvider(
        LocalMpeixPalette provides if (darkTheme) DarkMpeixPalette else LightMpeixPalette,
        LocalMpeixTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = remember {
                Typography(
                    headlineLarge = typography.header1,
                    headlineMedium = typography.header2,
                    headlineSmall = typography.header4,
                    titleLarge = typography.header3,
                    titleMedium = typography.paragraphBigAccent,
                    titleSmall = typography.paragraphNormalAccent,
                    bodyLarge = typography.paragraphBig,
                    bodyMedium = typography.paragraphNormal,
                    bodySmall = typography.labelBig,
                    labelLarge = typography.labelBig,
                    labelMedium = typography.labelNormal,
                    labelSmall = typography.labelMini,
                )
            },
            content = content,
        )
    }
}

public object MpeixTheme {

    public val palette: MpeixPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalMpeixPalette.current

    public val typography: MpeixTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMpeixTypography.current
}

internal val LocalMpeixPalette = staticCompositionLocalOf<MpeixPalette> {
    error("StaticCompositionLocal LocalMpeixPalette not provided")
}

internal val LocalMpeixTypography = staticCompositionLocalOf<MpeixTypography> {
    error("StaticCompositionLocal LocalMpeixTypography not provided")
}
