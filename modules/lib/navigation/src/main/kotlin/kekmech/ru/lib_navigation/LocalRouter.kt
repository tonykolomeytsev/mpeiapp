package kekmech.ru.lib_navigation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalRouter: ProvidableCompositionLocal<Router> = staticCompositionLocalOf {
    error("No LocalRouter provided!")
}
