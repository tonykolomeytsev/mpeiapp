package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kekmech.ru.domain_schedule.dto.Classes
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note_preview.*

data class NotePreview(
    val preview: String,
    val linkedClasses: Classes
)

interface NotePreviewViewHolder : ClickableItemViewHolder {
    fun setText(text: String)
}

class NotePreviewViewHolderImpl(
    override val containerView: View
) : NotePreviewViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    LayoutContainer {

    override fun setText(text: String) {
        textViewNoteCloud.text = text
    }
}

class ClassesNoteItemBinder(
    private val onClickListener: ((Classes) -> Unit)? = null
) : BaseItemBinder<NotePreviewViewHolder, NotePreview>() {

    override fun bind(vh: NotePreviewViewHolder, model: NotePreview, position: Int) {
        vh.setText(model.preview)
        onClickListener?.let { vh.setOnClickListener { it(model.linkedClasses) } }
    }
}

class NotePreviewAdapterItem(
    onClickListener: ((Classes) -> Unit)? = null,
    @LayoutRes layoutRes: Int = R.layout.item_note_preview
) : AdapterItem<NotePreviewViewHolder, NotePreview>(
    isType = { it is NotePreview },
    layoutRes = layoutRes,
    viewHolderGenerator = ::NotePreviewViewHolderImpl,
    itemBinder = ClassesNoteItemBinder(onClickListener)
)