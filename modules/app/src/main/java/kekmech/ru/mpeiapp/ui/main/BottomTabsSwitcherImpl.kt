package kekmech.ru.mpeiapp.ui.main

import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object BottomTabsSwitcherImpl : BottomTabsSwitcher {

    // StateFlow сразу требует стартовое значение
    private val _state = MutableStateFlow<BottomTab?>(null)
    val state: StateFlow<BottomTab?> = _state.asStateFlow()

    override fun changeTab(tab: BottomTab) {
        _state.value = tab
    }

    override fun clearTab() {
        _state.value = null
    }

    override fun observe(): StateFlow<BottomTab?> = state
}