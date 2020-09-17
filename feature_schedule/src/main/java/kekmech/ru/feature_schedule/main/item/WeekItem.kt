package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_days.*
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
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), WeekViewHolder, LayoutContainer {

    private var onDayClickListener: (DayItem) -> Unit = {}
    private var adapter: BaseAdapter? = null

    override fun setDays(items: List<Any>) {
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
            recyclerView.setHasFixedSize(true)
        }
    }

    override fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        recyclerView.setRecycledViewPool(recycledViewPool)
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