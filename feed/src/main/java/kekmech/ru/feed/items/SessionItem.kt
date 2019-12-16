package kekmech.ru.feed.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class SessionItem : BaseItem<SessionItem.ViewHolder>() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SessionDisciplineItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        adapter.baseItems.addAll(listOf(
            SessionDisciplineItem(),
            SessionDisciplineItem(),
            SessionDisciplineItem(),
            SessionDisciplineItem(),
            SessionDisciplineItem(),
            SessionDisciplineItem()
        ))
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        viewHolder.recycler.adapter = adapter
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_session, ::ViewHolder)
}