package kekmech.ru.timetable.view.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.timetable.R

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

    class Factory : BaseFactory(R.layout.item_lunch_min_layout, ::ViewHolder)

    /**
     * Оптимизация и анимация
     */
    override fun equals(other: Any?) = other is MinLunchItem
    override fun hashCode() = "THIS IS THE LUNCH, DOOMER".hashCode()
}