package kekmech.ru.feed.items

import android.view.View
import android.widget.ImageView
import kekmech.ru.coreui.adapter.BaseViewHolder2
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class WeekendItem(val text: String) : BaseItem2<WeekendItem.ViewHolder>(
    R.layout.item_weekend_feed_layout,
    ViewHolder::class
) {
    private val descriptions = listOf(
        "Нам пока нечего показать вам) Как насчёт полистать мемы?",
        "Сходи погуляй или посмотри ютуб. Или поставь лайк в нашей группе"
    )

    override fun updateViewHolder(vh: ViewHolder) {
        val resource = listOf(
            R.drawable.emoji_smile,
            R.drawable.emoji_love,
            R.drawable.emoji_hooray,
            R.drawable.emoji_stars,
            R.drawable.emoji_deal_with,
            R.drawable.emoji_cucumber,
            R.drawable.emoji_confetti
        ).random()
        vh.emoji.setImageResource(resource)
        vh.desc = descriptions.random()
        vh.head = "$text не надо на пары"
    }

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        val emoji by bind<ImageView>(R.id.imageViewMeme)
        var desc by bindText(R.id.textViewMessageNothingToShow)
        var head by bindText(R.id.textViewHeader1)
    }

    override fun equals(other: Any?) = other is WeekendItem
    override fun hashCode() = javaClass.hashCode()
}