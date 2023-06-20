package kekmech.ru.ui_icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MpeiX Icons Library
 */
object MpeixIcons {

    val WhatshotBlack24
        @Composable get() = painterResource(R.drawable.ic_whatshot_black_24)

    val WhatshotOutline24
        @Composable get() = painterResource(R.drawable.ic_whatshot_outline_24)

    val EventBlack24
        @Composable get() = painterResource(R.drawable.ic_event_black_24)

    val EventOutline24
        @Composable get() = painterResource(R.drawable.ic_event_outline_24)

    val ExploreBlack24
        @Composable get() = painterResource(R.drawable.ic_explore_black_24)

    val ExploreOutline24
        @Composable get() = painterResource(R.drawable.ic_explore_outline_24)

    val AccountBlack24
        @Composable get() = painterResource(R.drawable.ic_account_black_24)

    val AccountOutline24
        @Composable get() = painterResource(R.drawable.ic_account_outline_24)

    val Close24
        @Composable get() = painterResource(R.drawable.ic_close_24)

    val Search24
        @Composable get() = painterResource(R.drawable.ic_search_24)

    val PersonOutline24
        @Composable get() = painterResource(R.drawable.ic_person_outline_24)

    val GroupsOutline24
        @Composable get() = painterResource(R.drawable.ic_groups_outline_24)
}

@Composable
@Preview
@Suppress("MagicNumber")
fun IconsPreview() {
    val iconItem: LazyGridScope.(String, @Composable () -> Painter) -> Unit = { name, painter ->
        item(name) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp),
            ) {
                Icon(painter = painter.invoke(), contentDescription = null, tint = Color.Black)
                Text(text = name, fontSize = 8.sp, textAlign = TextAlign.Center)
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text("Icons 24x24", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(6)
        ) {
            iconItem("Whatshot\nBlack24") { MpeixIcons.WhatshotBlack24 }
            iconItem("Whatshot\nOutilne24") { MpeixIcons.WhatshotOutline24 }
            iconItem("Event\nBlack24") { MpeixIcons.EventBlack24 }
            iconItem("Event\nOutilne24") { MpeixIcons.EventOutline24 }
            iconItem("Explore\nBlack24") { MpeixIcons.ExploreBlack24 }
            iconItem("Explore\nOutilne24") { MpeixIcons.ExploreOutline24 }
            iconItem("Account\nBlack24") { MpeixIcons.AccountBlack24 }
            iconItem("Account\nOutilne24") { MpeixIcons.AccountOutline24 }
            iconItem("Close24") { MpeixIcons.Close24 }
            iconItem("Search24") { MpeixIcons.Search24 }
            iconItem("Person\nOutline24") { MpeixIcons.PersonOutline24 }
            iconItem("Groups\nOutilne24") { MpeixIcons.GroupsOutline24 }
        }
    }
}
