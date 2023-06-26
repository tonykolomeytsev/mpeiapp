package kekmech.ru.mpeiapp.demo.screens.typography

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.parcelize.Parcelize

@Parcelize
internal class TypographyScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { TypographyScreen() }
}


@Composable
private fun TypographyScreen() {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "Typography",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIconButton {
                        navigator.back()
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}
