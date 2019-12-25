package kekmech.ru.settings.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kekmech.ru.core.Router
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.items.DividerItem
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.coreui.items.TwoLineItem
import kekmech.ru.settings.R
import kekmech.ru.settings.SettingsPresenter
import kekmech.ru.settings.adapter.SingleLineTumblerItem
import kekmech.ru.settings.adapter.VersionItem
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SingleLineItem.Factory())
        .registerViewTypeFactory(TwoLineItem.Factory())
        .registerViewTypeFactory(DividerItem.Factory())
        .registerViewTypeFactory(SingleLineTumblerItem.Factory())
        .registerViewTypeFactory(VersionItem.Factory())
        .build()

    val router: Router by inject()
    val presenter: SettingsPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        view.recyclerView.adapter = adapter
        view.toolbar.setNavigationOnClickListener { router.popBackStack() }
        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.baseItems.clear()
        adapter.baseItems.addAll(
            listOf(
                SingleLineTumblerItem("Тёмная тема"),
                DividerItem("Хранилище"),
                TwoLineItem("Удалить все расписания" to "Будут также удалены и домашние задания", presenter::clearSchedules),
                SingleLineItem("Удалить все домашние задания", presenter::clearNotes, false),
                DividerItem("Личный кабинет БАРС"),
                SingleLineItem("Выйти из кабинета и стереть все данные", presenter::logout, false),
                DividerItem("Поддержка приложения"),
                TwoLineItem("Присоединиться к разработке" to "https://github.com/tonykolomeytsev/mpeiapp", presenter::github),
                TwoLineItem("Задать вопрос" to "https://vk.com/kekmech", presenter::vk, false),
                VersionItem()
            )
        )
        adapter.notifyDataSetChanged()
    }

    private fun BaseClickableItem<*>.oncl(action: (BaseClickableItem<*>) -> Unit) =
        this.apply { clickListener = { action(it) } }
}
