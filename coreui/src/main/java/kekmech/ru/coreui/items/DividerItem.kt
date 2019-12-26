package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class DividerItem(
    private val header: String
) : BaseItem<DividerItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.header.text = header
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val header by bind<TextView>(R.id.textViewDivider)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_divider_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}