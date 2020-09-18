package kekmech.ru.settings.adapter

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.AppVersion
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseItem
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder
import kekmech.ru.settings.R

class VersionItem(val appVersion: AppVersion) : BaseItem<VersionItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.title.text = "Mpeix v${appVersion.name} (${appVersion.number}) for Android"
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val title by bind<TextView>(R.id.textViewVersion)
    }

    class Factory : BaseFactory(
        R.layout.item_version,
        VersionItem::ViewHolder
    )
}