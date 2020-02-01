package kekmech.ru.timetable.view.items

import android.view.View
import android.view.View.VISIBLE
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.*
import kekmech.ru.timetable.R

class MinCoupleItem(val coupleNative: CoupleNative) : BaseClickableItem<MinCoupleItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name = coupleNative.name
        viewHolder.type = getStringType(viewHolder, coupleNative.type)

        if (coupleNative.place.isNotBlank()) {
            viewHolder.place.text = coupleNative.place
            viewHolder.place.visibility = View.VISIBLE
        } else {
            viewHolder.place.visibility = View.GONE
        }
        if (coupleNative.teacher.isNotBlank()) {
            viewHolder.teacher.text = coupleNative.teacher
            viewHolder.teacher.visibility = View.VISIBLE
        } else {
            viewHolder.teacher.visibility = View.GONE
        }

        viewHolder.timeStart = coupleNative.timeStart
        viewHolder.timeEnd = coupleNative.timeEnd
        viewHolder.number = "${coupleNative.num} ПАРА"

        coupleNative.noteLiveData.observeForever {
            animateView(viewHolder.hasNote, it != null)
        }
    }

    private fun animateView(view: View, visibility: Boolean) {
        if (view.alpha != 1f && visibility) {
            view.animate().alpha(1f).setDuration(200).start()
        } else if (view.alpha == 1f && !visibility) {
            view.animate().alpha(0f).setDuration(200).start()
        }
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

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        var name by bindText(R.id.textViewCoupleName)
        val place by bind<TextView>(R.id.textViewCouplePlace)
        var type by bindText(R.id.textViewCoupleType)
        val teacher by bind<TextView>(R.id.textViewCoupleTeacher)
        var timeStart by bindText(R.id.textViewCoupleTimeStart)
        var timeEnd by bindText(R.id.textViewCoupleTimeEnd)
        var hasNote by bind<TextView>(R.id.frameLayoutHasNote)
        var number by bindText(R.id.textViewCoupleNumber)

        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_couple_min_layout, ::ViewHolder)

    /**
     * Оптимизация и анимация
     */
    override fun equals(other: Any?) = if (other is MinCoupleItem) other.coupleNative.content == coupleNative.content else false
    override fun hashCode() = coupleNative.content.hashCode()

    private val CoupleNative.content get() = "$name $teacher $place $timeStart $timeEnd $type $day $noteId"
}