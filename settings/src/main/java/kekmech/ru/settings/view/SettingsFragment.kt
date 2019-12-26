package kekmech.ru.settings.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kekmech.ru.core.Router
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.items.DividerItem
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.coreui.items.TwoLineItem
import kekmech.ru.settings.R
import kekmech.ru.settings.SettingsPresenter
import kekmech.ru.settings.adapter.SingleLineTumblerItem
import kekmech.ru.settings.adapter.VersionItem
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.ext.android.inject


class SettingsFragment : BaseFragment<SettingsPresenter, SettingsFragmentView>(
    layoutId = R.layout.fragment_settings
), SettingsFragmentView {

    val router: Router by inject()
    override val presenter: SettingsPresenter by inject()

    override fun onResume() {
        super.onResume()
        recyclerView?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView?.adapter = presenter.adapter
        toolbar?.setNavigationOnClickListener { router.popBackStack() }
    }

    private fun BaseClickableItem<*>.oncl(action: (BaseClickableItem<*>) -> Unit) =
        this.apply { clickListener = { action(it) } }
}
