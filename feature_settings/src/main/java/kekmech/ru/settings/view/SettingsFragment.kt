package kekmech.ru.settings.view


import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.core.Router
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.settings.R
import kekmech.ru.settings.SettingsPresenter
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject


class SettingsFragment : BaseFragment<SettingsPresenter, SettingsFragmentView>(
    layoutId = R.layout.fragment_settings
), SettingsFragmentView {

    val router: Router by inject()
    override val presenter: SettingsPresenter by inject()

    override fun onResume() {
        super.onResume()
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = presenter.adapter
        toolbar?.setNavigationOnClickListener { router.popBackStack() }
    }

    override val recreatingActivity: AppCompatActivity
        get() = activity as AppCompatActivity
}
