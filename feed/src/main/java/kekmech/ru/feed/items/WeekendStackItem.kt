package kekmech.ru.feed.items

import android.view.View
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
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val spacing by bind<View>(R.id.weekendItemSpacing)
        val header by bind<TextView>(R.id.textViewWeekendHeader)
        val footer by bind<TextView>(R.id.textViewWeekendFooter)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_weekend_stack_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}