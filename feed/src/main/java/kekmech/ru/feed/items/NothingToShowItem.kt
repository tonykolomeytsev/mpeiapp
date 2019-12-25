package kekmech.ru.feed.items

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class NothingToShowItem : BaseItem<NothingToShowItem.ViewHolder>() {

    private val replices = listOf(
        R.drawable.emoji_alien to "ߛϠߚϞϠϩϞߚϩϞϿϩ",
        R.drawable.emoji_empty to "Соблюдайте пустоту",
        R.drawable.emoji_hooray to "Поздравляем!!!",
        R.drawable.emoji_gendalf to "Ты не пройдешь!",
        R.drawable.emoji_stupid to "Что-то пошло не так)))0))",
        R.drawable.emoji_robot to "Восстание машин!",
        R.drawable.emoji_russkie to "Русские вперед!"
    )

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val randomMeme = replices.random()
        viewHolder.img.setImageResource(randomMeme.first)
        viewHolder.title.text = randomMeme.second
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val title by bind<TextView>(R.id.textViewHeader1)
        val img by bind<ImageView>(R.id.imageViewMeme)
    }

    class Factory : BaseFactory(R.layout.item_nothing_to_show, ::ViewHolder)
}