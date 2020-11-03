package kekmech.ru.common_navigation

import androidx.appcompat.app.AppCompatActivity

interface NavigationHolder {

    fun subscribe(activity: AppCompatActivity)

    fun unsubscribe()
}