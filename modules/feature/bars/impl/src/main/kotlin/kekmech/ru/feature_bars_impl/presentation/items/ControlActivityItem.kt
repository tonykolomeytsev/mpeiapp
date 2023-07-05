package kekmech.ru.feature_bars_impl.presentation.items

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.databinding.ItemControlActivityBinding
import kekmech.ru.feature_bars_impl.domain.ControlActivity
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.coreui.R as coreui_R

internal class ControlActivityAdapterItem : AdapterItem<ControlActivityViewHolder, ControlActivity>(
    isType = { it is ControlActivity },
    layoutRes = R.layout.item_control_activity,
    viewHolderGenerator = ::ControlActivityViewHolder,
    itemBinder = ControlActivityItemBinder()
)

internal class ControlActivityViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemControlActivityBinding.bind(itemView)
    private val context get() = itemView.context

    fun setDeadline(deadline: String) {
        if (deadline.isNotBlank()) {
            val prefix = "Срок: "
            viewBinding.deadline.isVisible = true
            viewBinding.deadline.text = SpannableStringBuilder()
                .append(prefix)
                .append(
                    deadline,
                    ForegroundColorSpan(context.getThemeColor(coreui_R.attr.colorBlack)),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                .apply {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        prefix.length,
                        prefix.length + deadline.length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
        } else {
            viewBinding.deadline.isVisible = false
        }
    }

    fun setWeight(weight: String) {
        if (weight.isNotEmpty()) {
            val prefix = "Вес: "
            viewBinding.weight.isVisible = true
            viewBinding.weight.text = SpannableStringBuilder()
                .append(prefix)
                .append(
                    weight,
                    ForegroundColorSpan(context.getThemeColor(coreui_R.attr.colorBlack)),
                    SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                )
                .apply {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        prefix.length,
                        prefix.length + weight.length,
                        SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
                .append("%")
        } else {
            viewBinding.weight.isVisible = false
        }
    }

    fun setName(name: String) {
        viewBinding.name.text = name
    }

    fun setMark(mark: Float) {
        val inflater = LayoutInflater.from(viewBinding.root.context)
        val marksBinder = MarkItemBinder()
        with(viewBinding.markContainer) {
            removeAllViews()
            val view = inflater.inflate(R.layout.item_mark, this, false)
            addView(view)
            val vh = MarkViewHolder(view)
            marksBinder.bind(vh, MarkItem(mark), 0)
        }
    }

    fun clearMark() {
        viewBinding.markContainer.removeAllViews()
    }
}

internal class ControlActivityItemBinder :
    BaseItemBinder<ControlActivityViewHolder, ControlActivity>() {

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
