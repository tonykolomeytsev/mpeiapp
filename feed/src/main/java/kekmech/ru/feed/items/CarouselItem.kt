package kekmech.ru.feed.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class CarouselItem(
    val carousel: FeedCarousel,
    val picasso: PicassoFirebaseInstance
) : BaseItem2<CarouselItem.ViewHolder>(R.layout.item_carousel, ViewHolder::class) {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(CarouselPieceItem.Factory())
        .build()

    override fun updateViewHolder(vh: ViewHolder) {
        val items = carousel.items.filter { it.type != "debug" }.sortedBy { it.index }
        adapter.baseItems.addAll(items.map { CarouselPieceItem(it, picasso) } )
        vh.recycler.layoutManager = LinearLayoutManager(vh.recycler.context, LinearLayoutManager.HORIZONTAL, false)
        vh.recycler.adapter = adapter
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    override fun equals(other: Any?) = if (other is CarouselItem) other.carousel == carousel else false
    override fun hashCode() = carousel.hashCode()
}