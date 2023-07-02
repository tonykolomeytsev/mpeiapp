package kekmech.ru.library_schedule.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.ext_android.getResColor
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder
import kekmech.ru.library_schedule.R
import kekmech.ru.library_schedule.databinding.ItemNotePreviewBinding
import kekmech.ru.library_schedule.drawable.ProgressBackgroundDrawable
import kekmech.ru.coreui.R as coreui_R

data class NotePreview(
    val preview: String,
    val linkedClasses: Classes,
    val progress : Float? = null
)

interface NotePreviewViewHolder : ClickableItemViewHolder {
    fun setText(text: String)
    fun setProgress(progress: Float?)
}

class NotePreviewViewHolderImpl(
    private val containerView: View
) : NotePreviewViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemNotePreviewBinding.bind(containerView)

    override fun setText(text: String) {
        viewBinding.textViewNoteCloud.text = text
    }

    override fun setProgress(progress: Float?) {
        val context = containerView.context
        val bg = containerView
        if (progress == null) {
            bg.setBackgroundResource(coreui_R.drawable.background_classes_stack_end)
            return
        }
        if (bg.background !is ProgressBackgroundDrawable) {
            val dp7 = context.resources.dpToPx(PROGRESS_BACKGROUND_CORNER_RADIUS).toFloat()
            val progressBackgroundDrawable = ProgressBackgroundDrawable(
                context,
                context.getThemeColor(coreui_R.attr.colorGray10),
                context.getResColor(coreui_R.color.colorMain),
                cornerRadius = ProgressBackgroundDrawable.CornerRadius(
                    topLeft = 0f,
                    topRight = 0f,
                    bottomRight = dp7,
                    bottomLeft = dp7
                )
            )
            bg.background = progressBackgroundDrawable
            progressBackgroundDrawable.progress = progress
        } else {
            val progressBackgroundDrawable = bg.background as ProgressBackgroundDrawable
            progressBackgroundDrawable.progress = progress
        }
    }

    private companion object {
        private const val PROGRESS_BACKGROUND_CORNER_RADIUS = 7f
    }
}

class ClassesNoteItemBinder(
    private val onClickListener: ((Classes) -> Unit)? = null
) : BaseItemBinder<NotePreviewViewHolder, NotePreview>() {

    override fun bind(vh: NotePreviewViewHolder, model: NotePreview, position: Int) {
        vh.setText(model.preview)
        vh.setProgress(model.progress)
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
    itemBinder = ClassesNoteItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.preview == b.preview }
)
