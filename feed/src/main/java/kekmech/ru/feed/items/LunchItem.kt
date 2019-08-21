package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class LunchItem(val couple: CoupleNative) : BaseItem<LunchItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.timeStart.text = "12:45"
        viewHolder.timeEnd.text = "13:45"
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val timeStart by bind<TextView>(R.id.textViewCoupleTimeStart)
        val timeEnd by bind<TextView>(R.id.textViewCoupleTimeEnd)
        override fun onCreateView(view: View) {}
    }

    class Factory: BaseFactory(R.layout.item_lunch_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}