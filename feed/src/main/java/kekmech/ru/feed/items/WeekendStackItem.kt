package kekmech.ru.feed.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class WeekendStackItem(
    private val headerUpper: String,
    private val headerBottom: String,
    private val isLiveVisible: Boolean = true
) : BaseItem<WeekendStackItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.header.text = headerUpper
        viewHolder.footer.text = headerBottom
        viewHolder.spacing.visibility = if (isLiveVisible) View.VISIBLE else View.GONE
        listOf(
            R.drawable.emoji_smile,
            R.drawable.emoji_love,
            R.drawable.emoji_hooray,
            R.drawable.emoji_stars,
            R.drawable.emoji_deal_with,
            R.drawable.emoji_cucumber,
            R.drawable.emoji_confetti
        )
            .shuffled()
            .take(3)
            .forEachIndexed { i, e -> viewHolder.emojis[i].setImageResource(e) }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val spacing by bind<View>(R.id.weekendItemSpacing)
        val header by bind<TextView>(R.id.textViewWeekendHeader)
        val footer by bind<TextView>(R.id.textViewWeekendFooter)
        val emoji1 by bind<ImageView>(R.id.emoji1)
        val emoji2 by bind<ImageView>(R.id.emoji2)
        val emoji3 by bind<ImageView>(R.id.emoji3)
        val emojis by lazy { arrayOf(emoji1, emoji2, emoji3) }
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_weekend_stack_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}