package kekmech.ru.feed.items

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class TomorrowCouplesItem(val couples: List<CoupleNative>) : BaseItem<TomorrowCouplesItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val adapter = BaseAdapter.Builder()
            .registerViewTypeFactory(TomorrowCouplesDisciplineItem.Factory())
            .registerViewTypeFactory(TomorrowCouplesDecorItem.Factory())
            .build()
        val decorText = getDecorText(viewHolder.itemView.context)
        if (decorText.isNotEmpty()) adapter.baseItems += TomorrowCouplesDecorItem(decorText)

        adapter.baseItems += couples.map(::TomorrowCouplesDisciplineItem)
        viewHolder.recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        viewHolder.recyclerView.adapter = adapter
        viewHolder.header.setText(R.string.tomorrow_couples_header)
    }

    private fun getDecorText(context: Context): String {
        val minNum = couples.minBy { it.num }?.num ?: 0
        return Resources.getStringArray(context, R.array.tomorrow_couples_offer).getOrNull(minNum - 1) ?: ""
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val recyclerView by bind<RecyclerView>(R.id.recyclerView)
        val header by bind<TextView>(R.id.textViewHeader1)
    }

    class Factory: BaseFactory(R.layout.item_card_with_recycler, ::ViewHolder)
}