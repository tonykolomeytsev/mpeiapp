package kekmech.ru.coreui.items

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.R
import kekmech.ru.domain_notes.dto.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

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
    private val onClickListener: ((Note) -> Unit)?
) : BaseItemBinder<NoteViewHolder, Note>() {

    private val prettyDateFormatter = PrettyDateFormatter(context)

    override fun bind(vh: NoteViewHolder, model: Note, position: Int) {
        vh.setDisciplineName(model.classesName)
        vh.setContent(model.content)
        vh.setDate(prettyDateFormatter.formatRelative(model.dateTime.toLocalDate()))
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}

class NoteAdapterItem(
    context: Context,
    onClickListener: ((Note) -> Unit)? = null
) : AdapterItem<NoteViewHolder, Note>(
    isType = { it is Note },
    layoutRes = R.layout.item_note,
    viewHolderGenerator = ::NoteViewHolderImpl,
    itemBinder = NoteItemBinder(context, onClickListener)
)