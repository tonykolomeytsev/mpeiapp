package kekmech.ru.feature_dashboard.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.viewbinding.unit
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.databinding.ItemDayStatusBinding

private const val ANIMATION_DURATION = 200L

data class DayStatusItem(
    val header: String,
    val description: CharSequence
)

interface DayStatusViewHolder {
    fun setHeader(header: String)
    fun setDescription(description: CharSequence)
}

class DayStatusViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), DayStatusViewHolder {

    private val viewBinding = ItemDayStatusBinding.bind(containerView)

    override fun setHeader(header: String) {
        viewBinding.textViewHeader.text = header
    }

    override fun setDescription(description: CharSequence) = viewBinding.unit {
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

class DayStatusItemBinder : BaseItemBinder<DayStatusViewHolder, DayStatusItem>() {

    override fun bind(vh: DayStatusViewHolder, model: DayStatusItem, position: Int) {
        vh.setHeader(model.header)
        vh.setDescription(model.description)
    }
}

class DayStatusAdapterItem : AdapterItem<DayStatusViewHolder, DayStatusItem>(
    isType = { it is DayStatusItem },
    layoutRes = R.layout.item_day_status,
    viewHolderGenerator = ::DayStatusViewHolderImpl,
    itemBinder = DayStatusItemBinder(),
    areItemsTheSame = { a, b -> a.header == b.header }
)