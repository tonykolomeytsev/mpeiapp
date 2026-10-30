package kekmech.ru.ui_theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
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
import kekmech.ru.ui_theme.color.DarkMpeixScheme
import kekmech.ru.ui_theme.color.LightMpeixPalette
import kekmech.ru.ui_theme.color.LightMpeixScheme
import kekmech.ru.ui_theme.color.MpeixPalette
import kekmech.ru.ui_theme.typography.MpeixTypography

@Composable
public fun MpeixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkMpeixScheme else LightMpeixScheme
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
                    headlineLarge = typography.headlineBig,
                    headlineMedium = typography.headlineBig,
                    headlineSmall = typography.headlineSmall,
                    titleLarge = typography.titleLarge,
                    titleMedium = typography.titleMedium,
                    titleSmall = typography.titleSmall,
                    bodyLarge = typography.bodyLarge,
                    bodyMedium = typography.bodyMedium,
                    bodySmall = typography.bodySmall,
                    labelLarge = typography.labelLarge,
                    labelMedium = typography.labelMedium,
                    labelSmall = typography.labelSmall,
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
