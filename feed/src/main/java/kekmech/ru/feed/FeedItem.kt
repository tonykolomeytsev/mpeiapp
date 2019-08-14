package kekmech.ru.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.core.dto.Couple
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class FeedItem(val couple: Couple) : BaseItem<FeedItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.coupleName.text = couple.name
        viewHolder.coupleType.text = listOf("Практика", "Лекция").random()
        viewHolder.couplePlace.text = listOf("Б-407","Д-200", "Д-301", "Б-400").random()
        viewHolder.coupleTime.text = "11:10"
        //viewHolder.coupleTeacher.text = couple.teacher
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val coupleName by bind<TextView>(R.id.textViewCoupleName)
        val coupleType by bind<TextView>(R.id.textViewCoupleType)
        val couplePlace by bind<TextView>(R.id.textViewCouplePlace)
        val coupleTime by bind<TextView>(R.id.textViewCoupleTimeStart)
        //val coupleTeacher by bind<TextView>(R.id.textViewCoupleTeacher)
        override fun onCreateView(view: View) {}
    }

    class Factory: BaseFactory(R.layout.item_couple_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}