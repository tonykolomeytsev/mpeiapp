package kekmech.ru.feature_dashboard.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_dashboard.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_day_status.*

data class DayStatusItem(
    val header: String,
    val description: CharSequence
)

interface DayStatusViewHolder {
    fun setHeader(header: String)
    fun setDescription(description: CharSequence)
}

class DayStatusViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), DayStatusViewHolder, LayoutContainer {

    override fun setHeader(header: String) {
        textViewHeader.text = header
    }

    override fun setDescription(description: CharSequence) {
        if (textViewDescription.text.isEmpty() && description.isNotEmpty()) {
            textViewDescription.alpha = 0f
            textViewDescription.animate()
                .alpha(1f)
                .setDuration(200L)
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