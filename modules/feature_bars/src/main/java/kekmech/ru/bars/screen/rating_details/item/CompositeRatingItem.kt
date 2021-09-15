package kekmech.ru.bars.screen.rating_details.item

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.ItemCompositeRatingBinding
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_schedule.drawable.ProgressBackgroundDrawable

internal data class CompositeRatingItem(
    @StringRes val nameResId: Int,
    val value: CharSequence,
    val weight: Float,
    val progress: Float,
    val itemId: Int,
)

internal class CompositeRatingAdapterItem(
    onClick: (Int) -> Unit,
) : AdapterItem<CompositeRatingViewHolder, CompositeRatingItem>(
    isType = { it is CompositeRatingItem },
    layoutRes = R.layout.item_composite_rating,
    viewHolderGenerator = ::CompositeRatingViewHolder,
    itemBinder = CompositeRatingItemBinder(onClick)
)

@SuppressLint("ClickableViewAccessibility")
internal class CompositeRatingViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemCompositeRatingBinding.bind(itemView)

    init {
        val context = itemView.context
        @Suppress("MagicNumber")
        itemView.background =
            ProgressBackgroundDrawable(
                context,
                backgroundColor = context.getThemeColor(R.attr.colorGray10),
                progressColor = context.getResColor(R.color.colorMain),
                cornerRadius = ProgressBackgroundDrawable.CornerRadius
                    .of(context.resources.dpToPx(8f).toFloat())
            )
        itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                itemView.clearAnimation()
                itemView.animate()
                    .scaleX(ANIMATION_SCALE_MIN)
                    .scaleY(ANIMATION_SCALE_MIN)
                    .setDuration(ANIMATION_DURATION)
                    .start()
            } else {
                itemView.clearAnimation()
                itemView.animate()
                    .scaleX(ANIMATION_SCALE_DEFAULT)
                    .scaleY(ANIMATION_SCALE_DEFAULT)
                    .setDuration(ANIMATION_DURATION)
                    .start()
            }
            false
        }
    }

    fun setName(@StringRes nameResId: Int) {
        viewBinding.name.setText(nameResId)
    }

    fun setValue(value: CharSequence) {
        viewBinding.value.text = value
    }

    fun setProgress(progress: Float) {
        (viewBinding.root.background as? ProgressBackgroundDrawable)
            ?.progress = progress
    }

    fun setWeight(weight: Float) {
        viewBinding.weight.text = String.format("× %.1f", weight).replaceFirst(',', '.')
    }

    fun setOnClickListener(listener: () -> Unit) {
        viewBinding.root.setOnClickListener { listener.invoke() }
    }

    companion object {

        private const val ANIMATION_SCALE_DEFAULT = 1f
        private const val ANIMATION_SCALE_MIN = 0.97f
        private const val ANIMATION_DURATION = 100L
    }
}

private class CompositeRatingItemBinder(
    private val onClick: (Int) -> Unit,
) : BaseItemBinder<CompositeRatingViewHolder, CompositeRatingItem>() {

    override fun bind(vh: CompositeRatingViewHolder, model: CompositeRatingItem, position: Int) {
        vh.setName(model.nameResId)
        vh.setValue(model.value)
        vh.setProgress(model.progress)
        vh.setWeight(model.weight)
        vh.setOnClickListener { onClick.invoke(model.itemId) }
    }
}