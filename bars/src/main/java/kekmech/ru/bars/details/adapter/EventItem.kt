package kekmech.ru.bars.details.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.View.INVISIBLE
import android.widget.TextView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.ControlEvent
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kotlin.math.round

class EventItem(val event: ControlEvent) : BaseItem<EventItem.ViewHolder>() {

    var divider = true

    override fun updateViewHolder(viewHolder: ViewHolder) {
        val mk = event.mark
        if (mk == -1f) {
            viewHolder.mark.visibility = View.INVISIBLE
        } else {
            viewHolder.mark.visibility = View.VISIBLE
            viewHolder.mark.text = formatFloat(mk)
            when {
                (mk >= 0f) and (mk < 3f) -> {
                    viewHolder.mark.backgroundTintList = ColorStateList.valueOf(
                        Resources.getColor(
                            viewHolder.itemView.context,
                            R.color.markRed
                        )
                    )
                }
                (mk >= 3f) and (mk < 4f) -> {
                    viewHolder.mark.backgroundTintList = ColorStateList.valueOf(
                        Resources.getColor(
                            viewHolder.itemView.context,
                            R.color.markYellow
                        )
                    )
                }
                (mk >= 4f) -> {
                    viewHolder.mark.backgroundTintList = ColorStateList.valueOf(
                        Resources.getColor(
                            viewHolder.itemView.context,
                            R.color.markGreen
                        )
                    )
                }
            }
        }
        viewHolder.name.text = event.name
        viewHolder.weekNum.text = "${event.week} неделя"
        if (!divider) viewHolder.dividerBottom.visibility = INVISIBLE
    }

    private fun formatFloat(float: Float): String = if (round(float) == float) float.toInt().toString() else float.toString()

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewDisciplineName)
        val weekNum by bind<TextView>(R.id.textViewEventWeek)
        val mark by bind<TextView>(R.id.textViewMark)
        val dividerBottom by bind<View>(R.id.divider)
    }

    class Factory : BaseFactory(R.layout.item_events_table, ::ViewHolder)
}