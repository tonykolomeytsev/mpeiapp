package kekmech.ru.coreui.items

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*
import java.time.LocalDate

data class NoteItem(
    val content: String,
    val date: LocalDate,
    val disciplineName: String
)

interface NoteViewHolder : ClickableItemViewHolder {
    fun setContent(content: String)
    fun setDate(date: String)
    fun setDisciplineName(name: String)
}

class NoteViewHolderImpl(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    NoteViewHolder,
    LayoutContainer,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    override fun setContent(content: String) {
        textViewNoteContent.text = content
    }

    override fun setDate(date: String) {
        textViewNoteDate.text = date
    }

    override fun setDisciplineName(name: String) {
        textViewNoteDiscipline.text = name
    }
}

class NoteItemBinder(
    context: Context,
    private val onClickListener: ((NoteItem) -> Unit)?
) : BaseItemBinder<NoteViewHolder, NoteItem>() {

    private val prettyDateFormatter = PrettyDateFormatter(context)

    override fun bind(vh: NoteViewHolder, model: NoteItem, position: Int) {
        vh.setDisciplineName(model.disciplineName)
        vh.setContent(model.content)
        vh.setDate(prettyDateFormatter.formatRelative(model.date))
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}

class NoteAdapterItem(
    context: Context,
    onClickListener: ((NoteItem) -> Unit)? = null
) : AdapterItem<NoteViewHolder, NoteItem>(
    isType = { it is NoteItem },
    layoutRes = R.layout.item_note,
    viewHolderGenerator = ::NoteViewHolderImpl,
    itemBinder = NoteItemBinder(context, onClickListener)
)