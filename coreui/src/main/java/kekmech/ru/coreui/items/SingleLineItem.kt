package kekmech.ru.coreui.items

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.*

class SingleLineItem(
    val string: String, val onClick: (BaseItem<*>) -> Unit = {}, val d: Boolean = true
) : BaseClickableItem<SingleLineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textView.text = string
        viewHolder.divider.visibility = if (d) VISIBLE else INVISIBLE
        viewHolder.root.setOnClickListener { onClick(this) }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val textView by bind<TextView>(R.id.textViewPrimary)
        val divider by bind<View>(R.id.divider)
        val root by bind<FrameLayout>(R.id.rootView)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_single_line_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }

    companion object {
        fun buildAdapter() = BaseAdapter.Builder().registerViewTypeFactory(Factory()).build()
    }
}