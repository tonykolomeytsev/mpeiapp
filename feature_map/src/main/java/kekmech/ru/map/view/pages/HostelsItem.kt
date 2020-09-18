package kekmech.ru.map.view.pages

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map.R
import kekmech.ru.coreui.deprecated.adapter.BaseAdapter
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseItem
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder
import kekmech.ru.map.model.MapFragmentModel
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
                adapter.baseItems.addAll(it
                    .map(::SingleHostelItem)
                    .onEach { item -> item.clickListener = { model.selectedPlaceListener(item) } })
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

    class Factory : BaseFactory(R.layout.item_hostels, HostelsItem::ViewHolder)
}