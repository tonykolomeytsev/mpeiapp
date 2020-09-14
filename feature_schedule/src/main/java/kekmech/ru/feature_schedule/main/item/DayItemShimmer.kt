package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer

object DayShimmerItem

interface DayShimmerViewHolder

class DayShimmerViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), DayShimmerViewHolder, LayoutContainer

class DayShimmerItemBinder : BaseItemBinder<DayShimmerViewHolder, DayShimmerItem>() {

    override fun bind(vh: DayShimmerViewHolder, model: DayShimmerItem, position: Int) = Unit
}

class DayShimmerAdapterItem : AdapterItem<DayShimmerViewHolder, DayShimmerItem>(
    isType = { it is DayShimmerItem },
    layoutRes = R.layout.item_day_shimmer,
    viewHolderGenerator = ::DayShimmerViewHolderImpl,
    itemBinder = DayShimmerItemBinder()
)