package kekmech.ru.feature_main_screen_impl.presentation.navigation

import androidx.compose.runtime.Stable
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTabSwitcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
internal class MainScreenTabStateHolder : MainScreenTabSwitcher {

    private val _tabState = MutableStateFlow(MainScreenTab.Dashboard)
    val tabState: StateFlow<MainScreenTab> get() = _tabState

    override fun switch(tab: MainScreenTab) {
        _tabState.value = tab
    }
}
