package kekmech.ru.feed.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class CarouselItem : BaseItem<CarouselItem.ViewHolder>() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(CarouselPieceItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        adapter.baseItems.add(CarouselPieceItem("БАРС", R.drawable.bars_carousel_piece))
        adapter.baseItems.add(CarouselPieceItem("Расписание", R.drawable.timetable_carousel_piece))
        adapter.baseItems.add(CarouselPieceItem("Карта", R.drawable.map_carousel_piece))
        adapter.baseItems.add(CarouselPieceItem("Кекмех"))
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.recycler.context, LinearLayoutManager.HORIZONTAL, false)
        viewHolder.recycler.adapter = adapter
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_carousel, ::ViewHolder)
}