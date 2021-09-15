package kekmech.ru.bars.screen.rating_details.item

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
import kotlin.random.Random

internal data class CompositeRatingItem(
    @StringRes val nameResId: Int,
    val value: CharSequence,
    val weight: Float,
)

internal class CompositeRatingAdapterItem(
    onClick: () -> Unit,
) : AdapterItem<CompositeRatingViewHolder, CompositeRatingItem>(
    isType = { it is CompositeRatingItem },
    layoutRes = R.layout.item_composite_rating,
    viewHolderGenerator = ::CompositeRatingViewHolder,
    itemBinder = CompositeRatingItemBinder(onClick)
)

internal class CompositeRatingViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemCompositeRatingBinding.bind(itemView)

    init {
        val context = itemView.context
        @Suppress("MagicNumber")
        viewBinding.root.background =
            ProgressBackgroundDrawable(
                context,
                backgroundColor = context.getThemeColor(R.attr.colorGray10),
                progressColor = context.getResColor(R.color.colorMain),
                cornerRadius = ProgressBackgroundDrawable.CornerRadius
                    .of(context.resources.dpToPx(8f).toFloat())
            )
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
}

private class CompositeRatingItemBinder(
    onClick: () -> Unit,
) : BaseItemBinder<CompositeRatingViewHolder, CompositeRatingItem>() {

    override fun bind(vh: CompositeRatingViewHolder, model: CompositeRatingItem, position: Int) {
        vh.setName(model.nameResId)
        vh.setValue(model.value)
        vh.setProgress(Random.nextFloat())
    }
}