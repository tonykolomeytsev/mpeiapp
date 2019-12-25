package kekmech.ru.settings


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kekmech.ru.core.Router
import kekmech.ru.coreui.menu.BaseMenu
import kekmech.ru.coreui.menu.ItemListener
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment(), ItemListener {

    var statusBarColor: Int = 0

    val router: Router by inject()

    val adapter = BaseMenu(context)
        .divider("Основные", isLineVisible=false)
//        .item(
//            "Хранилище данных",
//            "Локальные файлы и БД",
//            R.drawable.outline_data_usage_black_24,
//            "STORAGE")
//        .item(
//            "Работа с сетью",
//            "Параметры и правила передачи данных",
//            R.drawable.outline_language_black_24,
//            "NETWORK")
//        .item(
//            "Расход батареи",
//            "Планировщик задач",
//            R.drawable.outline_offline_bolt_black_24,
//            "BATTERY")
//        .divider("Отладка")
//        .item(
//            "Параметры разработчиков",
//            "Функции для отладки",
//            R.drawable.outline_extension_black_24,
//            "DEV")
        .buildAdapter(this)

    override fun onItemClick(itemId: String) {
        when(itemId) {
            //"DEV" -> router.navigate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        view.recyclerView.adapter = adapter
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //statusBarColor = activity!!.window.statusBarColor
    }

    override fun onDetach() {
        super.onDetach()
        //activity!!.window.statusBarColor = statusBarColor
    }

}
