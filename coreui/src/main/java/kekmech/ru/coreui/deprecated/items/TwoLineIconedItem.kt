package kekmech.ru.coreui.deprecated.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.deprecated.adapter.BaseClickableItem
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder

class TwoLineIconedItem(
    private val header: String,
    private val description: String,
    private val icon: Int
) : BaseClickableItem<TwoLineIconedItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.description.text = description
        viewHolder.header.text = header
        viewHolder.icon.setImageResource(icon)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val header by bind<TextView>(R.id.textViewPrimary)
        val description by bind<TextView>(R.id.textViewSecondary)
        val icon by bind<ImageView>(R.id.imageViewAvatar)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_two_line_iconed_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}