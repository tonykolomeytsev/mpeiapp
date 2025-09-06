package kekmech.ru.mpeiapp.demo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kekmech.ru.mpeiapp.demo.navigation.LocalNavBackStack
import kekmech.ru.ui_kit_topappbar.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiKitScreen(
    title: String,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val backStack = LocalNavBackStack.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIconButton {
                        backStack.removeLastOrNull()
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
