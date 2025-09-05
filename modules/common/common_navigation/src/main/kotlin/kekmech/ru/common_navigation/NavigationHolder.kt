package kekmech.ru.common_navigation

import androidx.appcompat.app.AppCompatActivity

public interface NavigationHolder {

    public fun subscribe(activity: AppCompatActivity)

    public fun unsubscribe()
}
