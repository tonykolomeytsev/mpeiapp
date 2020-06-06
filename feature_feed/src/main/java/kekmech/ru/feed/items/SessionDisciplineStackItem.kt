package kekmech.ru.feed.items

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class SessionDisciplineStackItem(val events: Pair<AcademicSession.Event, AcademicSession.Event?>) : BaseItem<SessionDisciplineStackItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val first = events.first
        val second = events.second

        viewHolder.apply {
            val months = Resources.getStringArray(viewHolder.itemView.context, R.array.exam_months)
            name.text = first.name.substringBefore('(')

            type1.text = getTypeFrom(first.name)
            date1.text = formatDate(first.startDate, months)
            time1.text = first.startTime
            place1.text = first.place
            teacher1.text = first.teacher

            if (second == null) {
                section2.visibility = GONE
            } else {
                section2.visibility = VISIBLE

                type2.text = getTypeFrom(second.name)
                date2.text = formatDate(second.startDate, months)
                time2.text = second.startTime
                place2.text = second.place
                teacher2.text = second.teacher
            }
        }
    }

    private fun formatDate(startDate: String, months: Array<String>): String {
        val datePieces = startDate.split('.')
        val day = datePieces[0].toIntOrNull() ?: 1
        val month = months[(datePieces[1].toIntOrNull() ?: 1) - 1]
        return "$day $month"
    }

    private fun getTypeFrom(name: String) = name.substringAfter('(').substringBefore(')')

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewCoupleName)

        val type1 by bind<TextView>(R.id.textViewEventType1)
        val date1 by bind<TextView>(R.id.textViewEventDate1)
        val time1 by bind<TextView>(R.id.textViewEventTime1)
        val place1 by bind<TextView>(R.id.textViewCouplePlace1)
        val teacher1 by bind<TextView>(R.id.textViewCoupleTeacher1)

        val section2 by bind<LinearLayout>(R.id.linearLayoutSecondSection)

        val type2 by bind<TextView>(R.id.textViewEventType2)
        val date2 by bind<TextView>(R.id.textViewEventDate2)
        val time2 by bind<TextView>(R.id.textViewEventTime2)
        val place2 by bind<TextView>(R.id.textViewCouplePlace2)
        val teacher2 by bind<TextView>(R.id.textViewCoupleTeacher2)
    }

    class Factory : BaseFactory(R.layout.item_session_discipline_stack, ::ViewHolder)
}