package kekmech.ru.timetable.view.items

import android.view.View
import android.widget.ImageView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.timetable.R

class MinWeekendItem : BaseItem<MinWeekendItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val resource = listOf(
            R.drawable.emoji_smile,
            R.drawable.emoji_love,
            R.drawable.emoji_hooray,
            R.drawable.emoji_stars,
            R.drawable.emoji_deal_with,
            R.drawable.emoji_cucumber,
            R.drawable.emoji_confetti
        ).random()
        viewHolder.emoji.setImageResource(resource)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val emoji by bind<ImageView>(R.id.emoji)
        override fun onCreateView(view: View) = Unit
    }

    class Factory: BaseFactory(R.layout.item_weekend_min_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}