package kekmech.ru.settings


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.menu.BaseMenu
import kekmech.ru.coreui.menu.ItemListener
import kotlinx.android.synthetic.main.fragment_settings.view.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class SettingsFragment : DaggerFragment(), ItemListener {

    var statusBarColor: Int = 0

    @Inject
    lateinit var router: Router

    val adapter = BaseMenu(context)
        .divider("Основные", isLineVisible=false)
        .item(
            "Хранилище данных",
            "Локальные файлы и БД",
            R.drawable.outline_data_usage_black_24,
            "STORAGE")
        .item(
            "Работа с сетью",
            "Параметры и правила передачи данных",
            R.drawable.outline_language_black_24,
            "NETWORK")
        .item(
            "Расход батареи",
            "Планировщик задач",
            R.drawable.outline_offline_bolt_black_24,
            "BATTERY")
        .divider("Отладка")
        .item(
            "Параметры разработчиков",
            "Функции для отладки",
            R.drawable.outline_extension_black_24,
            "DEV")
        .buildAdapter(this)

    override fun onItemClick(itemId: String) {
        when(itemId) {
            "DEV" -> router.navigateTo("DEV")
        }
    }

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
        statusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = Resources.getColor(context, R.color.colorPrimary)
    }

    override fun onDetach() {
        super.onDetach()
        activity!!.window.statusBarColor = statusBarColor
    }

}
