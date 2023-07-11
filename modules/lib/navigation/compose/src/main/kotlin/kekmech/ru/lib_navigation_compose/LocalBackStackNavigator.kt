package kekmech.ru.lib_navigation_compose

import androidx.compose.runtime.staticCompositionLocalOf

val LocalBackStackNavigator = staticCompositionLocalOf<BackStackNavigator> {
    error("StaticCompositionalLocal BackStackNavigator not provided")
}