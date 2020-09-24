package kekmech.ru.common_navigation

import io.reactivex.Observable
import kekmech.ru.common_kotlin.Option

interface BottomTabsSwitcher {

    fun changeTab(tab: BottomTab)

    fun clearTab()

    fun observe(): Observable<Option<BottomTab>>
}