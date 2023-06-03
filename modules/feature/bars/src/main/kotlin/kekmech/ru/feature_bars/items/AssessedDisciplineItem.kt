package kekmech.ru.feature_bars.items

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.domain_bars.dto.AssessedDiscipline
import kekmech.ru.domain_bars.dto.FinalGradeType
import kekmech.ru.feature_bars.R
import kekmech.ru.feature_bars.databinding.ItemAssessedDisciplineBinding
import kekmech.ru.coreui.R as coreui_R

private const val BULLET_SEPARATOR = " • "
private const val DEFAULT_RIGHT_MARK_PADDING = 4

internal class AssessedDisciplineAdapterItem(
    onClickListener: (AssessedDiscipline) -> Unit
) : AdapterItem<AssessedDisciplineViewHolder, AssessedDiscipline>(
    isType = { it is AssessedDiscipline },
    layoutRes = R.layout.item_assessed_discipline,
    viewHolderGenerator = ::AssessedDisciplineViewHolder,
    itemBinder = AssessedDisciplineItemBinder(onClickListener)
)

internal class AssessedDisciplineViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(itemView) {

    private val viewBinding = ItemAssessedDisciplineBinding.bind(itemView)
    private val rightMarkPadding = itemView.resources.dpToPx(DEFAULT_RIGHT_MARK_PADDING)
    private val inflater = LayoutInflater.from(itemView.context)
    private val marksBinder = MarkItemBinder()

    fun setName(name: String) {
        viewBinding.disciplineName.text = name
    }

    fun setDescription(assessmentType: String, personName: String) {
        val context = viewBinding.root.context
        viewBinding.disciplineDescription.text = SpannableStringBuilder()
            .append(
                assessmentType,
                ForegroundColorSpan(context.getThemeColor(coreui_R.attr.colorBlack)),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            .append(BULLET_SEPARATOR)
            .append(personName)

    }

    fun clearMarks() {
        viewBinding.apply {
            finalMarkContainer.removeAllViews()
            marksContainer.removeAllViews()
        }
    }

    fun setMarks(marks: List<Float>) {
        with(viewBinding.marksContainer) {
            marks.forEachIndexed { index, mark ->
                val view = inflater.inflate(R.layout.item_mark, this, false)
                addView(view)
                val vh = MarkViewHolder(view)
                marksBinder.bind(vh, MarkItem(mark), index)
                view.setMargins(right = rightMarkPadding)
            }
        }
    }

    fun setEmptyMarks() {
        with(viewBinding.marksContainer) {
            val view = inflater.inflate(R.layout.item_no_marks, this, false)
            addView(view)
        }
    }

    fun setFinalMark(finalMark: Float) {
        with(viewBinding.finalMarkContainer) {
            val view = inflater.inflate(R.layout.item_mark, this, false) as TextView
            addView(view)
            val vh = MarkViewHolder(view)
            marksBinder.bind(vh, MarkItem(finalMark), 0)
            view.text = "Итог: ${view.text}"
            view.setMargins(right = rightMarkPadding)
        }
    }
}

internal class AssessedDisciplineItemBinder(
    private val onClickListener: (AssessedDiscipline) -> Unit
) : BaseItemBinder<AssessedDisciplineViewHolder, AssessedDiscipline>() {

    override fun bind(vh: AssessedDisciplineViewHolder, model: AssessedDiscipline, position: Int) {
        vh.setName(model.name)
        vh.setDescription(model.assessmentType, model.person)
        vh.setOnClickListener { onClickListener(model) }

        vh.clearMarks()
        val regularMarks = model.controlActivities
            .map { it.finalMark }
            .filter { it != -1f }
        val finalMark = model.finalGrades
            .firstOrNull { it.type == FinalGradeType.FINAL_MARK }
            ?.takeIf { it.finalMark != -1f }
        when {
            finalMark == null && regularMarks.isEmpty() -> vh.setEmptyMarks()
            else -> {
                vh.setMarks(regularMarks)
                finalMark?.finalMark?.let(vh::setFinalMark)
            }
        }
    }
}
