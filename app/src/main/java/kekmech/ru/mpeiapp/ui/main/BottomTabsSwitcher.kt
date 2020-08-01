package kekmech.ru.mpeiapp.ui.main

import io.reactivex.Observable
import kekmech.ru.common_kotlin.Option
import kekmech.ru.common_navigation.BottomTab

interface BottomTabsSwitcher {

    fun changeTab(tab: BottomTab)

    fun clearTab()

    fun observe(): Observable<Option<BottomTab>>
}