package kekmech.ru.feed.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class SessionItem(val academicSession: AcademicSession) : BaseItem2<SessionItem.ViewHolder>(R.layout.item_card_with_recycler, ViewHolder::class) {

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

    override fun updateViewHolder(vh: ViewHolder) {
        if (adapter.items.isEmpty()) adapter.baseItems.addAll(items)
        vh.recycler.layoutManager = LinearLayoutManager(vh.itemView.context)
        vh.recycler.adapter = adapter
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }


    override fun equals(other: Any?) = if (other is SessionItem) other.academicSession == academicSession else false
    override fun hashCode() = academicSession.hashCode()
}