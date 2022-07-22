package kekmech.ru.bars.items

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.ItemFinalGradeBinding
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.domain_bars.dto.FinalGrade
import kekmech.ru.domain_bars.dto.FinalGradeType

internal interface FinalGradeViewHolder {
    fun setName(name: CharSequence)
    fun setMark(mark: Float)
}

private class FinalGradeViewHolderImpl(
    itemView: View
) : RecyclerView.ViewHolder(itemView), FinalGradeViewHolder {

    private val viewBinding = ItemFinalGradeBinding.bind(itemView)

    override fun setName(name: CharSequence) {
        viewBinding.name.text = name
    }

    override fun setMark(mark: Float) {
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
}

internal class FinalGradeItemBinder : BaseItemBinder<FinalGradeViewHolder, FinalGrade>() {

    override fun bind(vh: FinalGradeViewHolder, model: FinalGrade, position: Int) {
        vh.setMark(model.finalMark)
        if (model.type == FinalGradeType.FINAL_MARK) {
            vh.setName(
                SpannableStringBuilder()
                    .append(
                        model.name,
                        StyleSpan(Typeface.BOLD),
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
            )
        } else {
            vh.setName(model.name)
        }
    }
}


internal class FinalGradeAdapterItem : AdapterItem<FinalGradeViewHolder, FinalGrade>(
    isType = { it is FinalGrade },
    layoutRes = R.layout.item_final_grade,
    viewHolderGenerator = ::FinalGradeViewHolderImpl,
    itemBinder = FinalGradeItemBinder()
)
