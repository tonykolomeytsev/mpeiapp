package kekmech.ru.bars.items

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.ItemControlActivityBinding
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.domain_bars.dto.ControlActivity

internal interface ControlActivityViewHolder {
    fun setName(name: String)
    fun setWeight(weight: String)
    fun setDeadline(deadline: String)
    fun setMark(mark: Float)
    fun clearMark()
}

private class ControlActivityViewHolderImpl(
    itemView: View
) : ControlActivityViewHolder, RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemControlActivityBinding.bind(itemView)
    private val context get() = itemView.context

    override fun setDeadline(deadline: String) {
        if (deadline.isNotBlank()) {
            val prefix = "Срок: "
            viewBinding.deadline.isVisible = true
            viewBinding.deadline.text = SpannableStringBuilder()
                .append(prefix)
                .append(deadline, ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                .apply {
                    setSpan(StyleSpan(Typeface.BOLD), prefix.length, prefix.length + deadline.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
        } else {
            viewBinding.deadline.isVisible = false
        }
    }

    override fun setWeight(weight: String) {
        if (weight.isNotEmpty()) {
            val prefix = "Вес: "
            viewBinding.weight.isVisible = true
            viewBinding.weight.text = SpannableStringBuilder()
                .append(prefix)
                .append(weight, ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                .apply {
                    setSpan(StyleSpan(Typeface.BOLD), prefix.length, prefix.length + weight.length, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                }
                .append("%")
        } else {
            viewBinding.weight.isVisible = false
        }
    }

    override fun setName(name: String) {
        viewBinding.name.text = name
    }

    override fun setMark(mark: Float) {
        val inflater = LayoutInflater.from(viewBinding.root.context)
        val marksBinder = MarkItemBinder()
        with(viewBinding.markContainer) {
            removeAllViews()
            val view = inflater.inflate(R.layout.item_mark, this, false)
            addView(view)
            val vh = MarkViewHolderImpl(view)
            marksBinder.bind(vh, MarkItem(mark), 0)
        }
    }

    override fun clearMark() {
        viewBinding.markContainer.removeAllViews()
    }
}

internal class ControlActivityItemBinder : BaseItemBinder<ControlActivityViewHolder, ControlActivity>() {

    override fun bind(vh: ControlActivityViewHolder, model: ControlActivity, position: Int) {
        if (model.finalMark != -1f) {
            vh.setMark(model.finalMark)
        } else {
            vh.clearMark()
        }
        vh.setName(model.name)
        vh.setWeight(model.weight)
        vh.setDeadline("${model.deadline} неделя")
    }
}

internal class ControlActivityAdapterItem : AdapterItem<ControlActivityViewHolder, ControlActivity>(
    isType = { it is ControlActivity },
    layoutRes = R.layout.item_control_activity,
    viewHolderGenerator = ::ControlActivityViewHolderImpl,
    itemBinder = ControlActivityItemBinder()
)