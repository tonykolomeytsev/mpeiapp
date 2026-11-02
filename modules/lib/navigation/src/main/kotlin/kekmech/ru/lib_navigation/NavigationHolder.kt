package kekmech.ru.lib_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

public interface NavigationHolder {

    public fun subscribe(activity: AppCompatActivity)

    public fun unsubscribe()

    public fun saveState(): Bundle

    public fun restoreState(state: Bundle)
}
