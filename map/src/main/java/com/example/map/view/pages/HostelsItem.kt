package com.example.map.view.pages

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map.R
import com.example.map.model.MapFragmentModel
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HostelsItem(private val model: MapFragmentModel) : BaseItem<HostelsItem.ViewHolder>() {

    private val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SingleHostelItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            model.hostels.observeForever {
                adapter.baseItems.clear()
                adapter.baseItems.addAll(it.map(::SingleHostelItem))
                viewHolder.recycler.adapter = adapter
                viewHolder.progressBar.visibility = View.INVISIBLE
            }
        }
    }
    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerViewHostels)
        val progressBar by bind<ProgressBar>(R.id.progressBar)
    }

    class Factory : BaseFactory(R.layout.item_hostels, ::ViewHolder)
}