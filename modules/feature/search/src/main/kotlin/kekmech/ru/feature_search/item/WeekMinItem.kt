package kekmech.ru.feature_search.item

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.databinding.ItemWeekMinBinding
import kekmech.ru.library_adapter.BaseAdapter
import kekmech.ru.library_adapter.BaseItemBinder
import kekmech.ru.library_schedule.items.DayAdapterItem
import kekmech.ru.library_schedule.items.DayItem

internal data class WeekMinItem(
    val days: List<DayItem>,
)

internal class WeekMinViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemWeekMinBinding.bind(itemView)

    fun update(adapter: BaseAdapter, days: List<DayItem>) {
        if (viewBinding.root.adapter == null) {
            viewBinding.root.adapter = adapter
        }
        adapter.update(days)
    }

}

internal class WeekMinItemBinder(
    context: Context,
    private val onDayClickListener: (DayItem) -> Unit,
) : BaseItemBinder<WeekMinViewHolder, WeekMinItem>() {

    private val adapter by fastLazy {
        BaseAdapter(
            DayAdapterItem(context, onDayClickListener, R.layout.item_day_min)
        )
    }

    override fun bind(vh: WeekMinViewHolder, model: WeekMinItem, position: Int) {
        vh.update(adapter, model.days)
    }
}
