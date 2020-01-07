package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class TomorrowCouplesDisciplineItem(val coupleNative: CoupleNative) : BaseItem<TomorrowCouplesDisciplineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.apply {
            name.text = coupleNative.name
            num.text = "${coupleNative.num} пара"
            time.text = coupleNative.timeStart
            place.text = coupleNative.place
            teacher.text = coupleNative.teacher
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewCoupleName)
        val num by bind<TextView>(R.id.textViewCoupleNumber)
        val time by bind<TextView>(R.id.textViewCoupleTime)
        val place by bind<TextView>(R.id.textViewCouplePlace)
        val teacher by bind<TextView>(R.id.textViewCoupleTeacher)
    }

    class Factory : BaseFactory(R.layout.item_tomorrow_couples_layout, ::ViewHolder)
}