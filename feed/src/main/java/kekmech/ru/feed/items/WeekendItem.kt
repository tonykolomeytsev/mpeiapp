package kekmech.ru.feed.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class WeekendItem() : BaseItem<WeekendItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val resource = listOf(
            kekmech.ru.coreui.R.drawable.emoji_smile,
            kekmech.ru.coreui.R.drawable.emoji_love,
            kekmech.ru.coreui.R.drawable.emoji_hooray,
            kekmech.ru.coreui.R.drawable.emoji_stars,
            kekmech.ru.coreui.R.drawable.emoji_deal_with,
            kekmech.ru.coreui.R.drawable.emoji_cucumber,
            kekmech.ru.coreui.R.drawable.emoji_confetti
        ).random()
        viewHolder.emoji.setImageResource(resource)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val emoji by bind<ImageView>(R.id.emoji)
        override fun onCreateView(view: View) = Unit
    }

    class Factory: BaseFactory(R.layout.item_weekend_full_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}