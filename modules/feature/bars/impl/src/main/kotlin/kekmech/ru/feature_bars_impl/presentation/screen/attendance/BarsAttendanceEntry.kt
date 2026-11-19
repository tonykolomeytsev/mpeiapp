package kekmech.ru.feature_bars_impl.presentation.screen.attendance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kekmech.ru.feature_bars_impl.navigation.BarsAttendanceScreenKey
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun BarsAttendanceEntry(key: BarsAttendanceScreenKey) {
    val router = LocalRouter.current
    val viewModel = koinViewModel<BarsAttendanceViewModel> { parametersOf(key) }
    val state by viewModel.state.collectAsState()
    BarsAttendanceScreen(
        state = state,
        onBackClick = { router.executeCommand(PopBackStack()) },
        onRefresh = viewModel::refresh,
        onReloadDisciplines = viewModel::reloadDisciplines,
        onReloadAbsences = viewModel::reloadAbsences,
    )
}
