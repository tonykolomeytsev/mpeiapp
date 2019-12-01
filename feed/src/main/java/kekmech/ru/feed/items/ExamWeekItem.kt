package kekmech.ru.feed.items

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class ExamWeekItem(private val context: Context) : BaseItem<ExamWeekItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.sessionHref.setOnClickListener {
            val url = "https://vk.com/thevyshka?w=wall-66036248_33757"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }

        viewHolder.barsHref.setOnClickListener {
            val url = "https://vk.com/@in_mpei-ballno-reitingovaya-sistema-v-mei"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val sessionHref by bind<TextView>(R.id.textViewHowNotToDieSession)
        val barsHref by bind<TextView>(R.id.textViewSessionRules)
    }

    class Factory : BaseFactory(R.layout.item_exam_week, ::ViewHolder)
}