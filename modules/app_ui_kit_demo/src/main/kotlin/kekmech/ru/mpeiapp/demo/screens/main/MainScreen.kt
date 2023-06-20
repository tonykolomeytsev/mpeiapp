package kekmech.ru.mpeiapp.demo.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_compose_theme.theme.MpeixTheme
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.mpeiapp.demo.screens.colors.ColorsScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.components.ComponentsScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.typography.TypographyScreenNavTarget
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kotlinx.parcelize.Parcelize

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
                title = { Text(text = greetings, style = MpeixTheme.typography.header3) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MpeixTheme.palette.background,
                    scrolledContainerColor = MpeixTheme.palette.surface,
                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item("typography") {
                SectionItem(
                    onClick = {
                        navigator.navigate(TypographyScreenNavTarget())
                    },
                    name = "Typography",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item("components") {
                SectionItem(
                    onClick = {
                        navigator.navigate(ComponentsScreenNavTarget())
                    },
                    name = "Components",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}
