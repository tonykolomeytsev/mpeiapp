package kekmech.ru.bars.main.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class DisciplinesItem(val disciplineItems: List<DisciplineItem>) : BaseItem<DisciplinesItem.ViewHolder>() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(DisciplineItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        adapter.baseItems.clear()
        adapter.baseItems.addAll(disciplineItems)
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        viewHolder.recycler.adapter = adapter
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_disciplines, ::ViewHolder)
}