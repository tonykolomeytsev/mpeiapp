package kekmech.ru.feed.items

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class CarouselPieceItem(
    val item: FeedCarousel.Item,
    val picasso: PicassoFirebaseInstance
) : BaseItem<CarouselPieceItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = item.name
        picasso.load(item.image_url, viewHolder.img)
        viewHolder.container.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.link)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            viewHolder.container.context.startActivity(i)
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val container by bind<CardView>(R.id.container)
        val name by bind<TextView>(R.id.textViewPieceName)
        val img by bind<ImageView>(R.id.imageViewPiece)
    }

    class Factory : BaseFactory(R.layout.item_carousel_piece, ::ViewHolder)
}