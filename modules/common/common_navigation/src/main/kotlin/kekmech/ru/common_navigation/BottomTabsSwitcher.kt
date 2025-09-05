package kekmech.ru.common_navigation

import io.reactivex.rxjava3.core.Observable
import java.util.*

public interface BottomTabsSwitcher {

    public fun changeTab(tab: BottomTab)

    public fun clearTab()

    public fun observe(): Observable<Optional<BottomTab>>
}
