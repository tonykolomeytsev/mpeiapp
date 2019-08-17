package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class TwoLineItem(val strings: Pair<String, String>) : BaseItem<TwoLineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textPrimary.text = strings.first
        viewHolder.textSecondary.text = strings.second
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val textPrimary by bind<TextView>(R.id.textViewPrimary)
        val textSecondary by bind<TextView>(R.id.textViewSecondary)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_two_line_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }

    companion object {
        fun buildAdapter() = BaseAdapter.Builder().registerViewTypeFactory(Factory()).build()
    }
}