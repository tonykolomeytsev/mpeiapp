package kekmech.ru.feed.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class SessionItem(val academicSession: AcademicSession) : BaseItem<SessionItem.ViewHolder>() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SessionDisciplineStackItem.Factory())
        .build()
    val items = mutableListOf<BaseItem<*>>()

    init {
        items.clear()
        val evs = academicSession.events

        var i = 0
        while (i < evs.size) {
            val name1 = evs[i].name.substringBefore('(').trim()
            val name2 = evs.getOrNull(i + 1)?.name?.substringBefore('(')?.trim() ?: ""
            if (name1 == name2) {
                items += SessionDisciplineStackItem(Pair(evs[i], evs[i + 1]))
                i += 2
            } else {
                items += SessionDisciplineStackItem(Pair(evs[i], null))
                i++
            }
        }
    }

    override fun updateViewHolder(viewHolder: ViewHolder) {
        adapter.baseItems.addAll(items)
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        viewHolder.recycler.adapter = adapter
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_card_with_recycler, ::ViewHolder)
}