package kekmech.ru.timetable

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class MinCoupleItem(val coupleNative: CoupleNative) : BaseItem<MinCoupleItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = coupleNative.name
        viewHolder.place.text = coupleNative.place
        viewHolder.type.text = getStringType(viewHolder, coupleNative.type, coupleNative.teacher)
        viewHolder.timeStart.text = coupleNative.timeStart
        viewHolder.timeEnd.text = coupleNative.timeEnd
    }

    private fun getStringType(
        viewHolder: ViewHolder,
        type: String,
        teacher: String
    ): String {
        val index = when(type) {
            CoupleNative.LECTURE -> 0
            CoupleNative.PRACTICE -> 1
            CoupleNative.LAB -> 2
            CoupleNative.COURSE -> 3
            else -> 4
        }
        val coupleType = Resources.getStringArray(viewHolder.itemView.context, R.array.couple_types)[index]
        val coupleTeacher = if (teacher.isNotBlank()) "- $teacher" else ""
        return "$coupleType $coupleTeacher"
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