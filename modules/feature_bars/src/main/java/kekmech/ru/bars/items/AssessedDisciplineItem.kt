package kekmech.ru.bars.items

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.ItemAssessedDisciplineBinding
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.domain_bars.dto.AssessedDiscipline

private const val BULLET_SEPARATOR = " • "
private const val DEFAULT_RIGHT_MARK_PADDING = 4

internal class AssessedDisciplineAdapterItem(
    onClickListener: (AssessedDiscipline) -> Unit
) : AdapterItem<AssessedDisciplineViewHolder, AssessedDiscipline>(
    isType = { it is AssessedDiscipline },
    layoutRes = R.layout.item_assessed_discipline,
    viewHolderGenerator = ::AssessedDisciplineViewHolderImpl,
    itemBinder = AssessedDisciplineItemBinder(onClickListener)
)

internal interface AssessedDisciplineViewHolder : ClickableItemViewHolder {
    fun setName(name: String)
    fun setDescription(assessmentType: String, personName: String)
    fun setMarks(marks: List<Float>)
}

private class AssessedDisciplineViewHolderImpl(
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    AssessedDisciplineViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(itemView) {

    private val viewBinding = ItemAssessedDisciplineBinding.bind(itemView)

    override fun setName(name: String) {
        viewBinding.disciplineName.text = name
    }

    override fun setDescription(assessmentType: String, personName: String) {
        val context = viewBinding.root.context
        viewBinding.disciplineDescription.text = SpannableStringBuilder()
            .append(assessmentType, ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(BULLET_SEPARATOR)
            .append(personName)

    }

    override fun setMarks(marks: List<Float>) {
        val inflater = LayoutInflater.from(viewBinding.root.context)
        val marksBinder = MarkItemBinder()
        with(viewBinding.marksContainer) {
            removeAllViews()
            marks.forEachIndexed { index, mark ->
                val view = inflater.inflate(R.layout.item_mark, this, false)
                addView(view)
                val vh = MarkViewHolderImpl(view)
                marksBinder.bind(vh, MarkItem(mark), index)
                view.setMargins(right = resources.dpToPx(DEFAULT_RIGHT_MARK_PADDING))
            }
            if (marks.isEmpty()) {
                val view = inflater.inflate(R.layout.item_no_marks, this, false)
                viewBinding.marksContainer.addView(view)
            }
        }
    }
}

internal class AssessedDisciplineItemBinder(
    private val onClickListener: (AssessedDiscipline) -> Unit
) : BaseItemBinder<AssessedDisciplineViewHolder, AssessedDiscipline>() {

    override fun bind(vh: AssessedDisciplineViewHolder, model: AssessedDiscipline, position: Int) {
        vh.setName(model.name)
        vh.setDescription(model.assessmentType, model.person)
        vh.setMarks(model.controlActivities.map { it.finalMark }.filter { it != -1f })
        vh.setOnClickListener { onClickListener(model) }
    }
}
