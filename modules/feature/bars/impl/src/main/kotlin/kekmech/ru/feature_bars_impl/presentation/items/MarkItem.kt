package kekmech.ru.feature_bars_impl.presentation.items

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.feature_bars_impl.databinding.ItemMarkBinding
import kekmech.ru.lib_adapter.BaseItemBinder
import kotlin.math.floor
import kekmech.ru.coreui.R as coreui_R

internal data class MarkItem(
    val mark: Float
)

internal class MarkViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemMarkBinding.bind(itemView)

    fun setMark(mark: String) {
        mark.let(viewBinding.root::setText)
    }

    fun setColors(backgroundColorAttr: Int, textColorAttr: Int) {
        with(viewBinding.root) {
            backgroundTintList = ColorStateList.valueOf(context.getThemeColor(backgroundColorAttr))
            setTextColor(context.getThemeColor(textColorAttr))
        }
    }
}

internal class MarkItemBinder : BaseItemBinder<MarkViewHolder, MarkItem>() {

    override fun bind(vh: MarkViewHolder, model: MarkItem, position: Int) {
        vh.setMark(model.mark.format())
        val (textColor, bgColor) = getColorsForMark(model.mark)
        vh.setColors(bgColor, textColor)
    }

    private fun Float.format(): String = if (floor(this) == this) {
        toInt().toString()
    } else {
        toString()
    }

    @Suppress("MagicNumber")
    private fun getColorsForMark(mark: Float): Pair<Int, Int> {
        return if (mark in 3.5f..5f) coreui_R.attr.colorMarkGreenText to coreui_R.attr.colorMarkGreenBg
        else if (3.5f > mark && mark >= 2.5f) coreui_R.attr.colorMarkYellowText to coreui_R.attr.colorMarkYellowBg
        else if (2.5f > mark && mark >= 0f) coreui_R.attr.colorMarkRedText to coreui_R.attr.colorMarkRedBg
        else coreui_R.attr.colorGray70 to coreui_R.attr.colorGray30
    }
}
