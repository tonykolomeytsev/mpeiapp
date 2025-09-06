package kekmech.ru.mpeiapp.demo.screens.components.topappbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.LocalNavBackStack
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_kit_topappbar.LargeTopAppBar
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.serialization.Serializable

@Serializable
internal object ComponentsTopAppBarNavTarget : NavScreen {

    @Composable
    override fun Content() {
        ComponentsTopAppBarScreen()
    }
}

@Serializable
internal object TopAppBarNavTarget : NavScreen {

    @Composable
    override fun Content() {
        TopAppBarScreen()
    }
}

@Serializable
internal object LargeTopAppBarNavTarget : NavScreen {

    @Composable
    override fun Content() {
        LargeTopAppBarScreen()
    }
}

@Composable
private fun ComponentsTopAppBarScreen() {
    val backStack = LocalNavBackStack.current

    UiKitScreen(title = "Top app bar") { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item {
                SectionItem(
                    onClick = {
                        backStack.add(TopAppBarNavTarget)
                    },
                    name = "TopAppBar (pinned)",
                )
            }
            item {
                SectionItem(
                    onClick = {
                        backStack.add(LargeTopAppBarNavTarget)
                    },
                    name = "LargeTopAppBar (enterAlways)",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarScreen() {
    val backStack = LocalNavBackStack.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "TopAppBar (pinned)",
                navigationIcon = {
                    BackIconButton {
                        backStack.removeLastOrNull()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LargeTopAppBarScreen() {
    val backStack = LocalNavBackStack.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = "LargeTopAppBar",
                navigationIcon = {
                    BackIconButton {
                        backStack.removeLastOrNull()
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
