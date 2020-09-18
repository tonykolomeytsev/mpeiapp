package kekmech.ru.bars.details.adapter

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import kekmech.ru.bars.R
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseItem
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder
import kotlin.math.round

class FinalItem(val text: String, val mk: Float) : BaseItem<FinalItem.ViewHolder>() {

    var divider = true

    override fun updateViewHolder(viewHolder: ViewHolder) {
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
        viewHolder.name.text = text
        if (!divider) viewHolder.dividerBottom.visibility = View.INVISIBLE
    }

    private fun formatFloat(float: Float): String = if (round(float) == float) float.toInt().toString() else float.toString()

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewDisciplineName)
        val mark by bind<TextView>(R.id.textViewMark)
        val dividerBottom by bind<View>(R.id.divider)
    }

    class Factory : BaseFactory(R.layout.item_final_table, ::ViewHolder)
}