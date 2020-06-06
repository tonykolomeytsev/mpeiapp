package kekmech.ru.bars.main.adapter

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import kekmech.ru.bars.R
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kotlin.math.round

class WeekMarkItem(val mk: Float) : BaseItem<WeekMarkItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        when {
            (mk >= 0f) and (mk < 3f) -> {
                viewHolder.mark.backgroundTintList = ColorStateList.valueOf(Resources.getColor(
                    viewHolder.itemView.context,
                    R.color.markRed
                ))
            }
            (mk >= 3f) and (mk < 4f) -> {
                viewHolder.mark.backgroundTintList = ColorStateList.valueOf(Resources.getColor(
                    viewHolder.itemView.context,
                    R.color.markYellow
                ))
            }
            (mk >= 4f) -> {
                viewHolder.mark.backgroundTintList = ColorStateList.valueOf(Resources.getColor(
                    viewHolder.itemView.context,
                    R.color.markGreen
                ))
            }
        }
    }
    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View): BaseViewHolder(view) {
        val mark by bind<View>(R.id.viewMark)
    }

    class Factory : BaseFactory(R.layout.item_week_mark, ::ViewHolder)
}