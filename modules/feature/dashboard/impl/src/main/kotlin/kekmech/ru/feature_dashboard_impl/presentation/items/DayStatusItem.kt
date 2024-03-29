package kekmech.ru.feature_dashboard_impl.presentation.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.feature_dashboard_impl.databinding.ItemDayStatusBinding
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

private const val ANIMATION_DURATION = 200L

internal data class DayStatusItem(
    val header: String,
    val description: CharSequence
)

internal class DayStatusAdapterItem : AdapterItem<DayStatusViewHolder, DayStatusItem>(
    isType = { it is DayStatusItem },
    layoutRes = R.layout.item_day_status,
    viewHolderGenerator = ::DayStatusViewHolder,
    itemBinder = DayStatusItemBinder(),
    areItemsTheSame = { a, b -> a.header == b.header }
)

internal class DayStatusViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemDayStatusBinding.bind(containerView)

    fun setHeader(header: String) {
        viewBinding.textViewHeader.text = header
    }

    fun setDescription(description: CharSequence) = with(viewBinding) {
        if (textViewDescription.text.isEmpty() && description.isNotEmpty()) {
            textViewDescription.alpha = 0f
            textViewDescription.animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .start()
        }
        textViewDescription.text = description
    }
}

private class DayStatusItemBinder : BaseItemBinder<DayStatusViewHolder, DayStatusItem>() {

    override fun bind(vh: DayStatusViewHolder, model: DayStatusItem, position: Int) {
        vh.setHeader(model.header)
        vh.setDescription(model.description)
    }
}
