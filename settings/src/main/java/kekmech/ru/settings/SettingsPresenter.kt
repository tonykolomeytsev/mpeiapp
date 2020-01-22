package kekmech.ru.settings

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.Observer
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.usecases.*
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.items.DividerItem
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.coreui.items.TwoLineItem
import kekmech.ru.settings.adapter.SingleLineTumblerItem
import kekmech.ru.settings.adapter.VersionItem
import kekmech.ru.settings.view.SettingsFragmentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsPresenter(
    private val context: Context,
    private val logOutUseCase: LogOutUseCase,
    private val router: Router,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val removeAllNotesUseCase: RemoveAllNotesUseCase,
    private val removeAllSchedulesUseCase: RemoveAllSchedulesUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val isDarkThemeEnabledUseCase: IsDarkThemeEnabledUseCase,
    private val setDarkThemeEnabledUseCase: SetDarkThemeEnabledUseCase
) : Presenter<SettingsFragmentView>() {
    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SingleLineItem.Factory())
        .registerViewTypeFactory(TwoLineItem.Factory())
        .registerViewTypeFactory(DividerItem.Factory())
        .registerViewTypeFactory(SingleLineTumblerItem.Factory())
        .registerViewTypeFactory(VersionItem.Factory())
        .build()

    override fun onResume(view: SettingsFragmentView) {
        super.onResume(view)

        val items = listOf(
            SingleLineTumblerItem("Тёмная тема", isDarkThemeEnabledUseCase()) { setDarkThemeEnabledUseCase(it, view.recreatingActivity) },
            TwoLineItem("Сменить группу" to "Загрузка...", ::changeGroup, false),
            DividerItem("Хранилище"),
            TwoLineItem("Удалить все расписания" to "Будут также удалены и все заметки", ::clearSchedules),
            SingleLineItem("Удалить все заметки", ::clearNotes, false),
            DividerItem("Личный кабинет БАРС"),
            SingleLineItem("Выйти из кабинета и стереть все данные", ::logout, false),
            DividerItem("Поддержка приложения"),
            TwoLineItem("Присоединиться к разработке" to "https://github.com/tonykolomeytsev/mpeiapp", ::github),
            TwoLineItem("Задать вопрос" to "https://vk.com/kekmech", ::vk, false),
            VersionItem(getAppVersionUseCase())
        )

        adapter.baseItems.clear()
        adapter.baseItems.addAll(items)
        adapter.notifyDataSetChanged()

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { getGroupNumberUseCase() }.observe(view, Observer {
                if (it != null && it.isNotEmpty()) {
                    (adapter.baseItems[1] as TwoLineItem).strings = "Сменить группу" to "Текущая группа: $it"
                } else {
                    (adapter.baseItems[1] as TwoLineItem).strings = "Сменить группу" to "Группа не выбрана"
                }
                adapter.notifyItemChanged(1)
            })
        }
    }




    fun changeGroup(item: BaseItem<*>) {
        router.navigate(Screens.SETTINGS_TO_ADD)
    }

    fun clearSchedules(item: BaseItem<*>) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { removeAllSchedulesUseCase() }
            Toast.makeText(context, "Все расписания стёрты", Toast.LENGTH_LONG).show()
            (adapter.baseItems[1] as TwoLineItem).strings = "Сменить группу" to "Группа не выбрана"
            adapter.notifyItemChanged(1)
        }
    }

    fun clearNotes(item: BaseItem<*>) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { removeAllNotesUseCase() }
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