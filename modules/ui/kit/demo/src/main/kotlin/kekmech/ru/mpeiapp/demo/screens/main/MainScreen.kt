package kekmech.ru.mpeiapp.demo.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.LocalNavBackStack
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.screens.colors.ColorsScreen
import kekmech.ru.mpeiapp.demo.screens.components.ComponentsScreen
import kekmech.ru.mpeiapp.demo.screens.elmslie.ElmDemoScreen
import kekmech.ru.mpeiapp.demo.screens.shimmers.ShimmersScreen
import kekmech.ru.mpeiapp.demo.screens.typography.TypographyScreen
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.mpeiapp.demo.ui.ToggleThemeActionButton
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
internal object MainScreen : NavScreen {

    @Composable
    override fun Content() {
        MainScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
private fun MainScreen() {
    val backStack = LocalNavBackStack.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "MpeiX Ui Kit Demo",
                scrollBehavior = scrollBehavior,
                actions = {
                    ToggleThemeActionButton()
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item("colors") {
                SectionItem(
                    onClick = {
                        backStack.add(ColorsScreen)
                    },
                    name = "Colors",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("typography") {
                SectionItem(
                    onClick = {
                        backStack.add(TypographyScreen)
                    },
                    name = "Typography",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("components") {
                SectionItem(
                    onClick = {
                        backStack.add(ComponentsScreen)
                    },
                    name = "Components",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("elmslie") {
                val randomArgument = remember { Random.nextInt() }
                SectionItem(
                    onClick = {
                        backStack.add(ElmDemoScreen(randomArgument = randomArgument))
                    },
                    name = "ELM Demo (arg = $randomArgument)",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("shimmers") {
                SectionItem(
                    onClick = {
                        backStack.add(ShimmersScreen)
                    },
                    name = "Shimmers",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
