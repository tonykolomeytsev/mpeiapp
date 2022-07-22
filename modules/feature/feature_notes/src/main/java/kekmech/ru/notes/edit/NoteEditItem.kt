package kekmech.ru.notes.edit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.afterTextChanged
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.databinding.ItemNoteEditBinding

internal interface NoteEditViewHolder {
    fun showKeyboard()
    fun setContent(content: String)
    fun afterTextChanged(listener: (String) -> Unit)
}

internal class NoteEditViewHolderImpl(
    itemView: View,
) : NoteEditViewHolder, RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemNoteEditBinding.bind(itemView)

    override fun showKeyboard() {
        viewBinding.editTextContent.showKeyboard()
    }

    override fun setContent(content: String) = with(viewBinding) {
        val selection = editTextContent.selectionStart to editTextContent.selectionEnd
        editTextContent.setText(content)
        editTextContent.setSelection(selection.first, selection.second)
    }

    override fun afterTextChanged(listener: (String) -> Unit) {
        viewBinding.editTextContent.afterTextChanged(listener)
    }
}

internal class NoteEditItemBinder(
    private val listener: (String) -> Unit,
) : BaseItemBinder<NoteEditViewHolder, Note>() {

    override fun bind(vh: NoteEditViewHolder, model: Note, position: Int) {
        vh.showKeyboard()
        vh.setContent(model.content)
        vh.afterTextChanged(listener)
    }

    override fun update(vh: NoteEditViewHolder, model: Note, position: Int, payloads: List<Any>) = Unit
}

internal class NoteEditAdapterItem(
    listener: (String) -> Unit,
) : AdapterItem<NoteEditViewHolder, Note>(
    isType = { it is Note },
    layoutRes = R.layout.item_note_edit,
    viewHolderGenerator = ::NoteEditViewHolderImpl,
    itemBinder = NoteEditItemBinder(listener),
    areItemsTheSame = { a, b -> a.id == b.id }
)
