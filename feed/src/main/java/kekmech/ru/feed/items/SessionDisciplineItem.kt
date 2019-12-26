package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class SessionDisciplineItem(val discipline: AcademicSession.Event) : BaseItem<SessionDisciplineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val months = Resources.getStringArray(viewHolder.itemView.context, R.array.exam_months)
        viewHolder.date.text = formatDate(discipline.startDate, months)
        viewHolder.startTime.text = discipline.startTime
        viewHolder.name.text = discipline.name
        viewHolder.teacher.text = discipline.teacher
        viewHolder.place.text = discipline.place
    }

    private fun formatDate(startDate: String, months: Array<String>): String {
        val datePieces = startDate.split('.')
        val day = datePieces[0].toIntOrNull() ?: 1
        val month = months[(datePieces[1].toIntOrNull() ?: 1) - 1]
        return "$day $month"
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val date by bind<TextView>(R.id.textViewCoupleDate)
        val startTime by bind<TextView>(R.id.textViewCoupleTimeStart)
        val name by bind<TextView>(R.id.textViewCoupleName)
        val teacher by bind<TextView>(R.id.textViewCoupleTeacher)
        val place by bind<TextView>(R.id.textViewCouplePlace)
    }

    class Factory : BaseFactory(R.layout.item_session_discipline, ::ViewHolder)

}