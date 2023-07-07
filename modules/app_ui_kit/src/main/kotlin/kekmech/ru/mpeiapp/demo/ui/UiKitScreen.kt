package kekmech.ru.mpeiapp.demo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kekmech.ru.lib_navigation_compose.LocalBackStackNavigator
import kekmech.ru.ui_kit_topappbar.TopAppBar

@Composable
fun UiKitScreen(
    title: String,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIconButton {
                        navigator.back()
                    }
                },
                actions = {
                    ToggleThemeActionButton()
                }
            )
        },
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}
