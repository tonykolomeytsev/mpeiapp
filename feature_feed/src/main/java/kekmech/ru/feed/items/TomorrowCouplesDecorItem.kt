package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class TomorrowCouplesDecorItem(val string: String) : BaseItem<TomorrowCouplesDecorItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.apply {
            name.text = string
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewCoupleName)
    }

    class Factory : BaseFactory(R.layout.item_tomorrow_couples_decor_layout, ::ViewHolder)
}