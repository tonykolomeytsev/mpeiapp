package kekmech.ru.mpeiapp.demo.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

@Stable
interface UiKitThemeController {

    fun darkThemeEnabled(): Boolean

    fun toggleDarkTheme()
}

val LocalUiKitThemeController = compositionLocalOf<UiKitThemeController> {
    error("LocalUiKitThemeController not provided")
}
