package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class EmptyItem(val onClickSubmit: () -> Unit) : BaseItem<EmptyItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.button.setOnClickListener { onClickSubmit() }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val button by bind<TextView>(R.id.buttonSubmit)
    }

    class Factory : BaseFactory(R.layout.item_empty_schedule, ::ViewHolder)
}