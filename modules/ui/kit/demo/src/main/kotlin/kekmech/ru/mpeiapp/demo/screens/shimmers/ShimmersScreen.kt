package kekmech.ru.mpeiapp.demo.screens.shimmers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.serialization.Serializable

@Serializable
internal object ShimmersScreen : NavScreen {

    @Composable
    override fun Content() {
        ShimmersScreen()
    }
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
            .padding(bottom = 8.dp)
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
