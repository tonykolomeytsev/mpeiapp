package kekmech.ru.mpeiapp.demo.screens.components.navigationbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_kit_navigationbar.floating.FloatingNavBar
import kotlinx.serialization.Serializable

@Serializable
internal object NavigationBarScreenNavTarget : NavScreen {

    @Composable
    override fun Content() {
        NavigationBarScreen()
    }
}

@Suppress("LongMethod", "MagicNumber")
@Composable
private fun NavigationBarScreen() {
    UiKitScreen(
        title = "Navigation bar",
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            var selectedItemIdx by remember { mutableIntStateOf(0) }
            FloatingNavBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Tab(
                    selected = selectedItemIdx == 0,
                    onClick = { selectedItemIdx = 0 },
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    label = "Главная",
                )
                Tab(
                    selected = selectedItemIdx == 1,
                    onClick = { selectedItemIdx = 1 },
                    iconSelected = Icons.Filled.DateRange,
                    iconUnselected = Icons.Outlined.DateRange,
                    label = "Расписание",
                )
                Tab(
                    selected = selectedItemIdx == 2,
                    onClick = { selectedItemIdx = 2 },
                    iconSelected = Icons.Filled.LocationOn,
                    iconUnselected = Icons.Outlined.LocationOn,
                    label = "Карта",
                )
                Tab(
                    selected = selectedItemIdx == 3,
                    onClick = { selectedItemIdx = 3 },
                    iconSelected = Icons.Filled.AccountCircle,
                    iconUnselected = Icons.Outlined.AccountCircle,
                    label = "Профиль",
                )
            }
        }
    }
}
