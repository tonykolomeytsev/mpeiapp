package kekmech.ru.timetable

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class MinCoupleItem(private val coupleNative: CoupleNative) : BaseItem<MinCoupleItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = coupleNative.name
        viewHolder.place.text = coupleNative.place
        viewHolder.type.text = coupleNative.type
        viewHolder.timeStart.text = coupleNative.timeStart
        viewHolder.timeEnd.text = coupleNative.timeEnd
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewCoupleName)
        val place by bind<TextView>(R.id.textViewCouplePlace)
        val type by bind<TextView>(R.id.textViewCoupleType)
        val timeStart by bind<TextView>(R.id.textViewCoupleTimeStart)
        val timeEnd by bind<TextView>(R.id.textViewCoupleTimeEnd)

        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_couple_min_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}