package kekmech.ru.mpeiapp.demo.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.library_navigation_api.NavTarget
import kekmech.ru.library_navigation_compose.LocalBackStackNavigator
import kekmech.ru.mpeiapp.demo.screens.colors.ColorsScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.components.ComponentsScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.elmslie.ElmDemoScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.typography.TypographyScreenNavTarget
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
internal class MainScreenNavTarget(private val greetings: String = "MpeiX UI-Kit") : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { MainScreen(greetings) }
}

@Composable
private fun MainScreen(greetings: String) {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = greetings,
                scrollBehavior = scrollBehavior,
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
                        navigator.navigate(ColorsScreenNavTarget())
                    },
                    name = "Colors",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("typography") {
                SectionItem(
                    onClick = {
                        navigator.navigate(TypographyScreenNavTarget())
                    },
                    name = "Typography",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("components") {
                SectionItem(
                    onClick = {
                        navigator.navigate(ComponentsScreenNavTarget())
                    },
                    name = "Components",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("elmslie") {
                val randomArgument = remember { Random.nextInt() }
                SectionItem(
                    onClick = {
                        navigator.navigate(ElmDemoScreenNavTarget(randomArgument = randomArgument))
                    },
                    name = "ELM Demo (arg = $randomArgument)",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
