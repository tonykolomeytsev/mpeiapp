package kekmech.ru.mpeiapp.demo.screens.colors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_compose_theme.theme.MpeixTheme
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ColorsScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ColorsScreen() }
}

@Composable
private fun ColorsScreen() {
    val navigator = LocalBackStackNavigator.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Colors", style = MpeixTheme.typography.header3) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MpeixTheme.palette.background,
                    scrolledContainerColor = MpeixTheme.palette.surface,
                ),
                navigationIcon = {
                    IconButton(onClick = { navigator.back() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}
