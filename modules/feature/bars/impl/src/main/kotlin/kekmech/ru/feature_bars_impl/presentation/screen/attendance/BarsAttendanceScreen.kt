package kekmech.ru.feature_bars_impl.presentation.screen.attendance

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar

@Composable
internal fun BarsAttendanceScreen() {
    val router = LocalRouter.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton { router.executeCommand(PopBackStack()) } },
                title = "Посещаемость",
            )
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            // TODO
        }
    }
}