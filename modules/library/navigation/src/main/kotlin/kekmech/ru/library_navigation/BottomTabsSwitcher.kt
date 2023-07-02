package kekmech.ru.library_navigation

import io.reactivex.rxjava3.core.Observable
import java.util.Optional

interface BottomTabsSwitcher {

    fun changeTab(tab: BottomTab)

    fun clearTab()

    fun observe(): Observable<Optional<BottomTab>>
}
