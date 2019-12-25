package kekmech.ru.settings

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import kekmech.ru.core.Presenter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.settings.view.SettingsFragmentView

class SettingsPresenter(
    private val context: Context
) : Presenter<SettingsFragmentView>() {

    fun clearSchedules(item: BaseItem<*>) {

    }

    fun clearNotes(item: BaseItem<*>) {

    }

    fun logout(item: BaseItem<*>) {

    }

    fun github(item: BaseItem<*>) {
        val url = "https://github.com/tonykolomeytsev/mpeiapp"
        val i = Intent(ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun vk(item: BaseItem<*>) {
        val url = "https://vk.com/kekmech"
        val i = Intent(ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }
}