package kekmech.ru.lib_navigation

import android.app.Activity
import android.os.Bundle

public interface NavigationHolder {

    public fun subscribe(activity: Activity)

    public fun unsubscribe()

    public fun saveState(): Bundle

    public fun restoreState(state: Bundle)
}
