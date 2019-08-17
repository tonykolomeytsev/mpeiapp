package kekmech.ru.settings

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class SettingsDividerItem(val header: String, val isLineVisible: Boolean = true) : BaseItem<SettingsDividerItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.header.text = header
        viewHolder.line.visibility = if (isLineVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val header by bind<TextView>(R.id.textViewDivider)
        val line by bind<View>(R.id.viewDivider)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_settings_divider_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}