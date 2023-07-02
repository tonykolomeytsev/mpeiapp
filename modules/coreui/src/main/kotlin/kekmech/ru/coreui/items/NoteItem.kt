package kekmech.ru.coreui.items

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.PrettyDateFormatter
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemNoteBinding
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder

class NoteAdapterItem(
    context: Context,
    onClickListener: ((Note) -> Unit)? = null
) : AdapterItem<NoteViewHolder, Note>(
    isType = { it is Note },
    layoutRes = R.layout.item_note,
    viewHolderGenerator = ::NoteViewHolder,
    itemBinder = NoteItemBinder(context, onClickListener),
    areItemsTheSame = { a, b -> a.id == b.id },
)

class NoteViewHolder(
    private val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemNoteBinding.bind(containerView)

    fun setContent(content: String) {
        viewBinding.textViewNoteContent.text = content
    }

    fun setDate(date: String) {
        viewBinding.textViewNoteDate.text = date
    }

    fun setDisciplineName(name: String) {
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
