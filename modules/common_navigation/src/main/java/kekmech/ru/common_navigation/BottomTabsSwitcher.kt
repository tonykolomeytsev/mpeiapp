package kekmech.ru.common_navigation

import io.reactivex.rxjava3.core.Observable
import java.util.*

interface BottomTabsSwitcher {

    fun changeTab(tab: BottomTab)

    fun clearTab()

    fun observe(): Observable<Optional<BottomTab>>
}