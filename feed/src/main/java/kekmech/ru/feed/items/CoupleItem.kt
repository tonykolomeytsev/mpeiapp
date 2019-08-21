package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.Couple
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.CoupleType
import kekmech.ru.coreui.Chip
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class CoupleItem(val couple: CoupleNative) : BaseItem<CoupleItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = couple.name ?: ""
        viewHolder.typeLecture.visibility = if (couple.type == CoupleNative.LECTURE) View.VISIBLE  else View.GONE
        viewHolder.typePractice.visibility = if (couple.type == CoupleNative.PRACTICE) View.VISIBLE  else View.GONE
        viewHolder.typeLab.visibility = if (couple.type == CoupleNative.LAB) View.VISIBLE else View.GONE
        viewHolder.overline.text = "Очередная пара" //couple.num
        viewHolder.place.text = couple.place ?: ""
        viewHolder.timeStart.text = couple.timeStart ?: ""
        viewHolder.timeEnd.text = couple.timeEnd ?: ""
        if (couple.teacher != null) {
            viewHolder.teacher.visibility = View.VISIBLE
            viewHolder.teacher.text = couple.teacher!!
        } else {
            viewHolder.teacher.visibility = View.GONE
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val name by bind<TextView>(R.id.textViewCoupleName)
        val overline by bind<TextView>(R.id.textViewCoupleOverline)
        val place by bind<TextView>(R.id.textViewCouplePlace)
        val timeStart by bind<TextView>(R.id.textViewCoupleTimeStart)
        val timeEnd by bind<TextView>(R.id.textViewCoupleTimeEnd)
        val teacher by bind<Chip>(R.id.chipCoupleTeacher)
        val typeLecture by bind<Chip>(R.id.chipCoupleLecture)
        val typePractice by bind<Chip>(R.id.chipCouplePractice)
        val typeLab by bind<Chip>(R.id.chipCoupleLab)
        override fun onCreateView(view: View) {}
    }

    class Factory: BaseFactory(R.layout.item_couple_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}