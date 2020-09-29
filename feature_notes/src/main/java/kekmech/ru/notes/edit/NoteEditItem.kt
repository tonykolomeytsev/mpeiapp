package kekmech.ru.notes.edit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.afterTextChanged
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note_edit.*

interface NoteEditViewHolder {
    fun showKeyboard()
    fun setContent(content: String)
    fun afterTextChanged(listener: (String) -> Unit)
}

class NoteEditViewHolderImpl(
    override val containerView: View
) : NoteEditViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun showKeyboard() {
        editTextContent.showKeyboard()
    }

    override fun setContent(content: String) {
        val selection = editTextContent.selectionStart to editTextContent.selectionEnd
        editTextContent.setText(content)
        editTextContent.setSelection(selection.first, selection.second)
    }

    override fun afterTextChanged(listener: (String) -> Unit) {
        editTextContent.afterTextChanged(listener)
    }
}

class NoteEditItemBinder(
    private val listener: (String) -> Unit
) : BaseItemBinder<NoteEditViewHolder, Note>() {

    override fun bind(vh: NoteEditViewHolder, model: Note, position: Int) {
        vh.showKeyboard()
        vh.setContent(model.content)
        vh.afterTextChanged(listener)
    }

    override fun update(vh: NoteEditViewHolder, model: Note, position: Int, payloads: List<Any>) = Unit
}

class NoteEditAdapterItem(
    listener: (String) -> Unit
) : AdapterItem<NoteEditViewHolder, Note>(
    isType = { it is Note },
    layoutRes = R.layout.item_note_edit,
    viewHolderGenerator = ::NoteEditViewHolderImpl,
    itemBinder = NoteEditItemBinder(listener),
    areItemsTheSame = { a, b -> a.id == b.id }
)