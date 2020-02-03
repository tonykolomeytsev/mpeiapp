package kekmech.ru.feed.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class NothingToShowItem : BaseItem2<NothingToShowItem.ViewHolder>(R.layout.item_nothing_to_show, ViewHolder::class) {

    private val replices = listOf(
        R.drawable.emoji_alien to "ߛϠߚϞϠϩϞߚϩϞϿϩ",
        R.drawable.emoji_empty to "Соблюдайте пустоту",
        R.drawable.emoji_hooray to "Поздравляем!!!",
        R.drawable.emoji_gendalf to "Ты не пройдешь!",
        R.drawable.emoji_stupid to "Что-то пошло не так)))0))",
        R.drawable.emoji_robot to "Восстание машин!",
        R.drawable.emoji_russkie to "Русские вперед!"
    )

    private val descriptions = listOf(
        "Нам пока нечего показать вам) Как насчёт полистать мемы?",
        "Чем меньше знаешь, тем крепче спишь. Оберегаем ваше сон)",
        "Что-то не получилось у нас ничего загрузить. Ну и пофиг.",
        "Даже роботы иногда факапят, прямо на ваших глазах 0_о..."
    )

    override fun updateViewHolder(vh: ViewHolder) {
        val randomMeme = replices.random()
        vh.img.setImageResource(randomMeme.first)
        vh.title.text = randomMeme.second
        vh.description.text = descriptions.random()
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val title by bind<TextView>(R.id.textViewHeader1)
        val description by bind<TextView>(R.id.textViewMessageNothingToShow)
        val img by bind<ImageView>(R.id.imageViewMeme)
    }
}