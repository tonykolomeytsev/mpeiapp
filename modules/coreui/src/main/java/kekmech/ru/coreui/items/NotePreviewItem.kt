package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note_preview.*

data class NotePreview(val preview: String)

interface NotePreviewViewHolder {
    fun setText(text: String)
}

class NotePreviewViewHolderImpl(
    override val containerView: View
) : NotePreviewViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun setText(text: String) {
        textViewNoteCloud.text = text
    }
}

class ClassesNoteItemBinder : BaseItemBinder<NotePreviewViewHolder, NotePreview>() {

    override fun bind(vh: NotePreviewViewHolder, model: NotePreview, position: Int) {
        vh.setText(model.preview)
    }
}

class NotePreviewAdapterItem : AdapterItem<NotePreviewViewHolder, NotePreview>(
    isType = { it is NotePreview },
    layoutRes = R.layout.item_note_preview,
    viewHolderGenerator = ::NotePreviewViewHolderImpl,
    itemBinder = ClassesNoteItemBinder()
)