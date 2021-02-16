package kekmech.ru.common_schedule.items

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.viewbinding.unit
import kekmech.ru.common_schedule.R
import kekmech.ru.common_schedule.databinding.ItemWeekDaysBinding
import java.time.LocalDate

data class WeekItem(
    val weekOffset: Int,
    val firstDayOfWeek: LocalDate?,
    val dayItems: List<DayItem>
)

interface WeekViewHolder {
    fun createAdapterIfNull()
    fun setDays(items: List<Any>)
    fun setOnDayClickListener(listener: (DayItem) -> Unit)
    fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool)
}

class WeekViewHolderImpl(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView), WeekViewHolder {

    private var onDayClickListener: (DayItem) -> Unit = {}
    private var adapter: BaseAdapter? = null
    private val viewBinding = ItemWeekDaysBinding.bind(containerView)

    override fun setDays(items: List<Any>) = viewBinding.unit {
        if (recyclerView.adapter == null) recyclerView.adapter = adapter
        if (recyclerView.layoutManager == null) recyclerView.layoutManager = GridLayoutManager(containerView.context, 6)
        adapter?.update(items)
    }

    override fun setOnDayClickListener(listener: (DayItem) -> Unit) {
        onDayClickListener = listener
    }

    override fun createAdapterIfNull() {
        if (adapter == null) {
            adapter = BaseAdapter(
                DayAdapterItem(
                    context = containerView.context,
                    onDayClickListener = { onDayClickListener(it) }
                )
            )
            viewBinding.recyclerView.setHasFixedSize(true)
        }
    }

    override fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        viewBinding.recyclerView.setRecycledViewPool(recycledViewPool)
    }
}

class WeekItemBinder(
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val onDayClickListener: (DayItem) -> Unit
) : BaseItemBinder<WeekViewHolder, WeekItem>() {

    override fun bind(vh: WeekViewHolder, model: WeekItem, position: Int) {
        vh.setRecycledViewPool(recycledViewPool)
        vh.createAdapterIfNull()
        vh.setDays(model.dayItems)
        vh.setOnDayClickListener(onDayClickListener)
    }
}

class WeekAdapterItem(
    onDayClickListener: (DayItem) -> Unit
) : AdapterItem<WeekViewHolder, WeekItem>(
    isType = { it is WeekItem },
    layoutRes = R.layout.item_week_days,
    viewHolderGenerator = ::WeekViewHolderImpl,
    itemBinder = WeekItemBinder(
        recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 200) },
        onDayClickListener = onDayClickListener
    )
)