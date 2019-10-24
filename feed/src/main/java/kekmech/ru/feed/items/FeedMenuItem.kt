package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.*
import kekmech.ru.feed.R

class FeedMenuItem(
    val string: String
) : BaseClickableItem<FeedMenuItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textView.text = string
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val textView by bind<TextView>(R.id.textViewPrimary)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_feed_menu_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }

    companion object {
        fun buildAdapter() = BaseAdapter.Builder().registerViewTypeFactory(Factory()).build()
    }
}