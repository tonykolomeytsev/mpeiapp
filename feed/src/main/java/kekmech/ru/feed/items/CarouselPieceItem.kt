package kekmech.ru.feed.items

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class CarouselPieceItem(val name: String, val drawable: Int = 0) : BaseItem<CarouselPieceItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = name
        if (drawable != 0) viewHolder.img.setImageResource(drawable)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val container by bind<LinearLayout>(R.id.container)
        val name by bind<TextView>(R.id.textViewPieceName)
        val img by bind<ImageView>(R.id.imageViewPiece)
    }

    class Factory : BaseFactory(R.layout.item_carousel_piece, ::ViewHolder)
}