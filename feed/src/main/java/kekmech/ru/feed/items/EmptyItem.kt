package kekmech.ru.feed.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.coreui.adapter2.BaseItem2
import kekmech.ru.feed.R

class EmptyItem(val onClickSubmit: () -> Unit) : BaseItem2<EmptyItem.ViewHolder>(R.layout.item_empty_schedule, ViewHolder::class) {

    override fun updateViewHolder(vh: ViewHolder) {
        vh.button.setOnClickListener { onClickSubmit() }
    }
    class ViewHolder(view: View) : BaseViewHolder(view) {
        val button by bind<TextView>(R.id.buttonSubmit)
    }
}