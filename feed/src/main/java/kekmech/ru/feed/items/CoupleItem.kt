package kekmech.ru.feed.items

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Chip
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class CoupleItem(val couple: CoupleNative) : BaseItem<CoupleItem.ViewHolder>() {
    var isDividerVisible = false

    @SuppressLint("DefaultLocale")
    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = couple.name
        viewHolder.typeLecture.visibility = if (couple.type == CoupleNative.LECTURE) View.VISIBLE  else View.GONE
        viewHolder.typePractice.visibility = if (couple.type == CoupleNative.PRACTICE) View.VISIBLE  else View.GONE
        viewHolder.typeLab.visibility = if (couple.type == CoupleNative.LAB) View.VISIBLE else View.GONE
        viewHolder.overline.text = getCoupleNumAsString(viewHolder.itemView.context).toUpperCase()
        viewHolder.place.text = couple.place
        viewHolder.timeStart.text = couple.timeStart
        viewHolder.timeEnd.text = couple.timeEnd
        viewHolder.divider.visibility = if (isDividerVisible) View.VISIBLE else View.INVISIBLE
        if (couple.teacher.isNotEmpty()) {
            viewHolder.teacher.visibility = View.VISIBLE
            viewHolder.teacher.text = couple.teacher
        } else viewHolder.teacher.visibility = View.GONE
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
        val divider by bind<View>(R.id.viewDivider)
        override fun onCreateView(view: View) {}
    }

    class Factory: BaseFactory(R.layout.item_couple_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }


    private fun getCoupleNumAsString(context: Context): String {
        if (couple.num in 1..5) {
            if (coupleNames == null) coupleNames = Resources.getStringArray(context, R.array.couples_numbers)
            return coupleNames!![couple.num - 1]
        } else {
            if (coupleUnknown == null) coupleUnknown = Resources.getString(context, R.string.couple_without_number)
            return coupleUnknown!!
        }
    }

    companion object {
        private var coupleNames: Array<String>? = null
        private var coupleUnknown: String? = null
    }
}