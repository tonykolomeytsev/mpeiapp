package kekmech.ru.mpeiapp.demo.screens.components.topappbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.lib_navigation_compose.LocalBackStackNavigator
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_kit_topappbar.LargeTopAppBar
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ComponentsTopAppBarNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ComponentsTopAppBarScreen() }
}

@Parcelize
internal class TopAppBarNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { TopAppBarScreen() }
}

@Parcelize
internal class LargeTopAppBarNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { LargeTopAppBarScreen() }
}

@Composable
private fun ComponentsTopAppBarScreen() {
    val navigator = LocalBackStackNavigator.current

    UiKitScreen(title = "Top app bar") { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item {
                SectionItem(
                    onClick = {
                        navigator.navigate(TopAppBarNavTarget())
                    },
                    name = "TopAppBar (pinned)",
                )
            }
            item {
                SectionItem(
                    onClick = {
                        navigator.navigate(LargeTopAppBarNavTarget())
                    },
                    name = "LargeTopAppBar (enterAlways)",
                )
            }
        }
    }
}

@Composable
private fun TopAppBarScreen() {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "TopAppBar (pinned)",
                navigationIcon = {
                    BackIconButton {
                        navigator.back()
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            @Suppress("MagicNumber")
            repeat(25) {
                item {
                    SectionItem(onClick = { /* no-op */ }, name = "Item $it")
                }
            }
        }
    }
}

@Composable
private fun LargeTopAppBarScreen() {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = "LargeTopAppBar",
                navigationIcon = {
                    BackIconButton {
                        navigator.back()
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            @Suppress("MagicNumber")
            repeat(25) {
                item {
                    SectionItem(onClick = { /* no-op */ }, name = "Item $it")
                }
            }
        }
    }
}
