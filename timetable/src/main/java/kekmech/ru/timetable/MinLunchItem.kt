package kekmech.ru.timetable

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class MinLunchItem() : BaseItem<MinLunchItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.timeStart.text = "12:45"
        viewHolder.timeEnd.text = "13:45"
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val timeStart by bind<TextView>(R.id.textViewCoupleTimeStart)
        val timeEnd by bind<TextView>(R.id.textViewCoupleTimeEnd)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_lunch_min_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}