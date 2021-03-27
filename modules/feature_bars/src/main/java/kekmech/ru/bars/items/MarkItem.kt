package kekmech.ru.bars.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.ItemMarkBinding
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getThemeColor
import kotlin.math.floor

internal data class MarkItem(
    val mark: Float
)

internal interface MarkViewHolder {
    fun setMark(mark: String)
    fun setColors(
        @AttrRes backgroundColorAttr: Int,
        @AttrRes textColorAttr: Int
    )
}

internal class MarkViewHolderImpl(
    itemView: View
) : MarkViewHolder, RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemMarkBinding.bind(itemView)

    override fun setMark(mark: String) {
        mark.let(viewBinding.root::setText)
    }

    override fun setColors(backgroundColorAttr: Int, textColorAttr: Int) {
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

    private fun getColorsForMark(mark: Float): Pair<Int, Int> {
        return if (mark in 3.5f..5f) R.attr.colorMarkGreenText to R.attr.colorMarkGreenBg
        else if (3.5f > mark && mark >= 2.5f) R.attr.colorMarkYellowText to R.attr.colorMarkYellowBg
        else if (2.5f > mark && mark >= 0f) R.attr.colorMarkRedText to R.attr.colorMarkRedBg
        else R.attr.colorGray70 to R.attr.colorGray30
    }
}