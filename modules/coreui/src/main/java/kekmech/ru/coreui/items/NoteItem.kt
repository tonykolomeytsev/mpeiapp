package kekmech.ru.coreui.items

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemNoteBinding
import kekmech.ru.domain_notes.dto.Note

interface NoteViewHolder : ClickableItemViewHolder {
    fun setContent(content: String)
    fun setDate(date: String)
    fun setDisciplineName(name: String)
}

class NoteViewHolderImpl(
    private val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    NoteViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemNoteBinding.bind(containerView)

    override fun setContent(content: String) {
        viewBinding.textViewNoteContent.text = content
    }

    override fun setDate(date: String) {
        viewBinding.textViewNoteDate.text = date
    }

    override fun setDisciplineName(name: String) {
        viewBinding.textViewNoteDiscipline.text = name
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