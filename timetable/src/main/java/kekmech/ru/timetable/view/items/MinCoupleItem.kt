package kekmech.ru.timetable.view.items

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.timetable.R

class MinCoupleItem(val coupleNative: CoupleNative) : BaseClickableItem<MinCoupleItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = coupleNative.name
        viewHolder.place.text = coupleNative.place
        viewHolder.type.text = getStringType(viewHolder, coupleNative.type)
        if (coupleNative.teacher.isNotBlank()) {
            viewHolder.teacher.text = coupleNative.teacher
            viewHolder.teacher.visibility = View.VISIBLE
        } else {
            viewHolder.teacher.visibility = View.GONE
        }
        viewHolder.timeStart.text = coupleNative.timeStart
        viewHolder.timeEnd.text = coupleNative.timeEnd
        viewHolder.number.text = "${coupleNative.num} ПАРА"

        if (coupleNative.noteId != -1)
            viewHolder.hasNoteLayout.visibility = VISIBLE
        else
            viewHolder.hasNoteLayout.visibility = GONE
    }

    private fun getStringType(
        viewHolder: ViewHolder,
        type: String
    ): String {
        val index = when(type) {
            CoupleNative.LECTURE -> 0
            CoupleNative.PRACTICE -> 1
            CoupleNative.LAB -> 2
            CoupleNative.COURSE -> 3
            else -> 4
        }
        return Resources.getStringArray(viewHolder.itemView.context,
            R.array.couple_types
        )[index]
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewCoupleName)
        val place by bind<TextView>(R.id.textViewCouplePlace)
        val type by bind<TextView>(R.id.textViewCoupleType)
        val teacher by bind<TextView>(R.id.textViewCoupleTeacher)
        val timeStart by bind<TextView>(R.id.textViewCoupleTimeStart)
        val timeEnd by bind<TextView>(R.id.textViewCoupleTimeEnd)
        val hasNoteLayout by bind<LinearLayout>(R.id.frameLayoutHasNote)
        val number by bind<TextView>(R.id.textViewCoupleNumber)

        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_couple_min_layout) {
        override fun instance(view: View) =
            ViewHolder(view)
    }
}