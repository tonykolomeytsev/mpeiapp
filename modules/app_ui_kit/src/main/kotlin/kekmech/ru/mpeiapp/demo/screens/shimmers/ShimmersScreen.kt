package kekmech.ru.mpeiapp.demo.screens.shimmers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ShimmersScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ShimmersScreen() }
}

@Composable
private fun ShimmersScreen() {
    UiKitScreen(title = "Shimmers") { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            ShimmerItem()
            ShimmerItem()
            ShimmerItem()
            ShimmerItem()
            ShimmerItem()
        }
    }
}

@Composable
private fun ShimmerItem() {
    val color = MpeixTheme.palette.shimmer
    val radius = 4.dp
    Row(
        Modifier
            .padding(bottom=8.dp)
            .padding(horizontal = 16.dp)
            .shimmer()
    ) {
        Box(
            Modifier
                .padding(end = 8.dp)
                .size(64.dp)
                .background(color, RoundedCornerShape(radius))
        )
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(18.dp)
                    .background(color, RoundedCornerShape(radius))
            )
            Box(
                Modifier
                    .fillMaxWidth(fraction = 0.75f)
                    .height(14.dp)
                    .background(color, RoundedCornerShape(radius))
            )
        }
    }
}
