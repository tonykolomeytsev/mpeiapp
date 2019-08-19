package kekmech.ru.settings


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.DaggerFragment
import io.realm.Realm
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.menu.ItemListener
import kekmech.ru.coreui.menu.BaseMenu
import kekmech.ru.repository.User
import kotlinx.android.synthetic.main.fragment_settings.view.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SettingsDevFragment : DaggerFragment(), ItemListener {

    var statusBarColor: Int = 0

    val adapter = BaseMenu(context)
        .divider("Realm", isLineVisible=false)
        .item(
            "Стереть базу данных",
            "realm.deleteAll()",
            R.drawable.outline_delete_forever_black_24,
            "REALM_DELETE")
        .item(
            "Посмотреть содержимое User",
            "realm.where(User).first()",
            R.drawable.outline_announcement_black_24,
            "REALM_WATCH")
        .item(
            "Инициализировать поля",
            "new RealmObject()",
            R.drawable.outline_refresh_black_24,
            "REALM_FILL")
        .item(
            "Стереть номер группы",
            "REALM_GROUP")
        .divider("Retrofit")
        .item(
            "Получить тестовый JSON",
            "Запрос на GitHub",
            R.drawable.outline_cloud_download_black_24,
            "RETROFIT_TEST")
        .buildAdapter(this)

    @Inject
    lateinit var realm: Realm

    @Inject
    lateinit var router: Router

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_dev, container, false)
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = adapter
        view.toolbar.setNavigationOnClickListener {
            it.postOnAnimation { router.exit() }
        }
        return view
    }

    override fun onItemClick(itemId: String) {
        when(itemId) {
            "REALM_DELETE" -> realm.executeTransaction {
                it.deleteAll()
                Toast.makeText(activity, "База данных приложения стёрта", Toast.LENGTH_SHORT).show()
            }
            "REALM_WATCH" -> {
                val user = realm
                    .where(User::class.java)
                    .findFirst()
                Toast.makeText(activity, user.toString(), Toast.LENGTH_LONG).show()
            }
            "REALM_FILL" -> {
                realm.executeTransaction {
                    val user = it.createObject(User::class.java)
                    user.firstLaunchFlag = false
                    user.developerMode = true
                }
            }
            "REALM_GROUP" -> {

            }
        }
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
