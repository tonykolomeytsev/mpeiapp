package kekmech.ru.common_schedule.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_schedule.R
import kekmech.ru.common_schedule.databinding.ItemNotePreviewBinding
import kekmech.ru.common_schedule.drawable.ProgressBackgroundDrawable
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.domain_schedule.dto.Classes

data class NotePreview(
    val preview: String,
    val linkedClasses: Classes
)

interface NotePreviewViewHolder : ClickableItemViewHolder {
    fun setText(text: String)
    fun setProgress(progress: Float)
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

    override fun setProgress(progress: Float) {
        val context = containerView.context
        val bg = containerView
        if (bg.background !is ProgressBackgroundDrawable) {
            val dp7 = context.resources.dpToPx(PROGRESS_BACKGROUND_CORNER_RADIUS).toFloat()
            val progressBackgroundDrawable = ProgressBackgroundDrawable(
                context,
                context.getThemeColor(R.attr.colorGray10),
                context.getResColor(R.color.colorMain),
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
        model.linkedClasses.progress?.let(vh::setProgress)
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