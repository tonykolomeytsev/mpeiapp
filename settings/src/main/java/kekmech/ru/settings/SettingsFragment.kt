package kekmech.ru.settings


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.coreui.adapter.BaseAdapter
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    val adapter = Menu(context)
        .divider("Основные", isLineVisible=false)
        .item("Хранилище данных", "Локальные файлы и БД", R.drawable.outline_data_usage_black_24)
        .item("Работа с сетью", "Параметры и правила передачи данных", R.drawable.outline_language_black_24)
        .item("Расход батареи", "Планировщик задач", R.drawable.outline_offline_bolt_black_24)
        .divider("Отладка")
        .item("Параметры разработчиков", "Функции для отладки", R.drawable.outline_extension_black_24)
        .buildAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = adapter
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity!!.window.statusBarColor = context!!.resources.getColor(R.color.colorPrimaryDark)
    }

    override fun onDetach() {
        super.onDetach()
        activity!!.window.statusBarColor = context!!.resources.getColor(R.color.colorBrackTransparent)
    }

}
