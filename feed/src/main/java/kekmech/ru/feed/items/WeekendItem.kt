package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class WeekendItem() : BaseItem<WeekendItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) = Unit

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onCreateView(view: View) = Unit
    }

    class Factory: BaseFactory(R.layout.item_weekend_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}