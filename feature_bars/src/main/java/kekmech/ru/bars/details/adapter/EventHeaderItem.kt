package kekmech.ru.bars.details.adapter

import android.view.View
import kekmech.ru.bars.R
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseItem
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder

class EventHeaderItem : BaseItem<EventHeaderItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) = Unit

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view)

    class Factory : BaseFactory(R.layout.item_events_table_header, ::ViewHolder)
}