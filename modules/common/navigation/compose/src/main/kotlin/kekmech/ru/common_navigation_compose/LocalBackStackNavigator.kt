package kekmech.ru.common_navigation_compose

import androidx.compose.runtime.staticCompositionLocalOf
import kekmech.ru.common_navigation_api.BackStackNavigator

val LocalBackStackNavigator = staticCompositionLocalOf<BackStackNavigator> {
    error("StaticCompositionalLocal BackStackNavigator not provided")
}
