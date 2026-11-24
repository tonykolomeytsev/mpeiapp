package kekmech.ru.mpeiapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntryDecorator
import kekmech.ru.lib_navigation.ScreenKey
import kekmech.ru.lib_navigation.bottomTab
import kekmech.ru.lib_navigation.isBottomSheet
import kekmech.ru.ui_kit_navigationbar.system.SystemNavBarScrim

/**
 * Draws a scrim under the 3-button system navigation on top of the screens.
 *
 * The scrim is a part of the screen, so it moves together with the screen in the transitions.
 * Tab screens are skipped: the floating navigation bar has its own scrim down to the bottom edge.
 * Bottom sheets are skipped too: they are drawn in their own window over the screen.
 */
@Composable
fun rememberSystemNavBarScrimNavEntryDecorator(): NavEntryDecorator<ScreenKey> {
    return remember {
        NavEntryDecorator { entry ->
            if (entry.bottomTab != null || entry.isBottomSheet) {
                entry.Content()
            } else {
                Box(Modifier.fillMaxSize()) {
                    entry.Content()
                    SystemNavBarScrim(Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }
}
