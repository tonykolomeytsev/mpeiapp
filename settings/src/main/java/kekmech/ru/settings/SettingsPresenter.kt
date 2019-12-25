package kekmech.ru.settings

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast
import kekmech.ru.core.Presenter
import kekmech.ru.core.usecases.GetAllSchedulesUseCase
import kekmech.ru.core.usecases.GetRatingUseCase
import kekmech.ru.core.usecases.LogOutUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.items.TwoLineItem
import kekmech.ru.settings.view.SettingsFragmentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsPresenter(
    private val context: Context,
    private val logOutUseCase: LogOutUseCase
) : Presenter<SettingsFragmentView>() {

    fun clearSchedules(item: BaseItem<*>) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, "Все расписания стёрты", Toast.LENGTH_LONG).show()
        }
    }

    fun clearNotes(item: BaseItem<*>) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, "Все домашние задания стёрты", Toast.LENGTH_LONG).show()
        }
    }

    fun logout(item: BaseItem<*>) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { logOutUseCase() }
            Toast.makeText(context, "Все учетные данные стёрты", Toast.LENGTH_LONG).show()
        }
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