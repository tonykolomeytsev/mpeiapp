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
        adapter.baseItems.add(CarouselPieceItem("Правила БАРС в этом семестре", R.drawable.bars_carousel_piece, "https://vk.com/@in_mpei-ballno-reitingovaya-sistema-v-mei"))
        adapter.baseItems.add(CarouselPieceItem("Как сдать сессию и не умереть", R.drawable.timetable_carousel_piece, "https://vk.com/thevyshka?w=wall-66036248_33757"))
        adapter.baseItems.add(CarouselPieceItem("Кекмех\nОбратная связь", 0, "https://vk.com/kekmech"))
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.recycler.context, LinearLayoutManager.HORIZONTAL, false)
        viewHolder.recycler.adapter = adapter
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_carousel, ::ViewHolder)
}