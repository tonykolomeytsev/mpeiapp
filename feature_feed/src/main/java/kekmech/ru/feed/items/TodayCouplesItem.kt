package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class TodayCouplesItem(val couples: List<CoupleNative>) : BaseItem2<TodayCouplesItem.ViewHolder>(R.layout.item_card_with_recycler, ViewHolder::class) {

    override fun updateViewHolder(vh: ViewHolder) {
        val adapter = BaseAdapter.Builder()
            .registerViewTypeFactory(TomorrowCouplesDisciplineItem.Factory())
            .build()

        adapter.baseItems += couples.map(::TomorrowCouplesDisciplineItem)
        vh.recyclerView.layoutManager = LinearLayoutManager(vh.itemView.context)
        vh.recyclerView.adapter = adapter
        vh.header.setText(R.string.today_couples_header)
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val recyclerView by bind<RecyclerView>(R.id.recyclerView)
        val header by bind<TextView>(R.id.textViewHeader1)
    }


    override fun equals(other: Any?) = if (other is TodayCouplesItem) other.couples == couples else false
    override fun hashCode() = couples.hashCode()
}