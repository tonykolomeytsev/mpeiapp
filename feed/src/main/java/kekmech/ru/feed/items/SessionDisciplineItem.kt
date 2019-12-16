package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class SessionDisciplineItem : BaseItem<SessionDisciplineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.date.text = "10 янв"
        viewHolder.startTime.text = "11:10"
        viewHolder.name.text = "Линейная алгебра"
        viewHolder.teacher.text = "Кудин С.Ф."
        viewHolder.place.text = "Б-407"
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