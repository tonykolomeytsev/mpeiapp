package kekmech.ru.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.core.dto.Couple
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class FeedItem(val couple: Couple) : BaseItem<FeedItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.primaryText.text = couple.name
        viewHolder.secondaryText.text = couple.teacher
        viewHolder.icon.setImageResource(R.drawable.img_template)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val primaryText by bind<TextView>(R.id.textViewPrimary)
        val secondaryText by bind<TextView>(R.id.textViewSecondary)
        val icon by bind<ImageView>(R.id.imageViewAvatar)
        override fun onCreateView(view: View) {}
    }

    class Factory: BaseFactory(R.layout.item_two_line_rounded_icon_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}