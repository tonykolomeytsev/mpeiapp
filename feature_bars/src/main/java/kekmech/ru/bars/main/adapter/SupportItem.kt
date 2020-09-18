package kekmech.ru.bars.main.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.view.View
import android.widget.TextView
import kekmech.ru.bars.R
import kekmech.ru.coreui.deprecated.adapter.BaseClickableItem
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder


class SupportItem(private val context: Context) : BaseClickableItem<SupportItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textViewHref.setOnClickListener {
            val url = "https://vk.com/kekmech"
            val i = Intent(ACTION_VIEW)
            i.data = Uri.parse(url)
            i.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View): BaseViewHolder(view) {
        val textViewHref by bind<TextView>(R.id.textViewHref)
    }

    class Factory : BaseFactory(R.layout.item_support, ::ViewHolder)


    /**
     * Для оптимизации главного экрана БАРСа
     */
    override fun equals(other: Any?) = other is SupportItem
    override fun hashCode() = javaClass.hashCode()
}