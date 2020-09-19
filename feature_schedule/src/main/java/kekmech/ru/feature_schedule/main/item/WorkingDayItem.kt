package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClassesAdapterItem
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_days.*

const val DAY_OF_WEEK_MONDAY = 1
const val DAY_OF_WEEK_TUESDAY = 2
const val DAY_OF_WEEK_WEDNESDAY = 3
const val DAY_OF_WEEK_THURSDAY = 4
const val DAY_OF_WEEK_FRIDAY = 5
const val DAY_OF_WEEK_SATURDAY = 6

data class WorkingDayItem(
    val dayOfWeek: Int,
    val items: List<Any> = emptyList()
)

interface WorkingDayViewHolder {
    fun setItems(list: List<Any>)
    fun initAdapter(recycledViewPool: RecyclerView.RecycledViewPool)
}

class WorkingDayViewHolderImpl(
    override val containerView: View
) : WorkingDayViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val adapter = BaseAdapter(
        ClassesAdapterItem(containerView.context), // viewType = 0
        ClassesStackStartAdapterItem(containerView.context),
        ClassesStackMiddleAdapterItem(containerView.context),
        ClassesStackEndAdapterItem(containerView.context),
        SelfStudyAdapterItem(),
        LunchAdapterItem()
    )

    override fun setItems(list: List<Any>) {
        adapter.update(list)
    }

    override fun initAdapter(recycledViewPool: RecyclerView.RecycledViewPool) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(containerView.context)
        recyclerView.setRecycledViewPool(recycledViewPool)
    }
}

class WorkingDayItemBinder(
    private val recycledViewPool: RecyclerView.RecycledViewPool
) : BaseItemBinder<WorkingDayViewHolder, WorkingDayItem>() {

    override fun bind(vh: WorkingDayViewHolder, model: WorkingDayItem, position: Int) {
        vh.initAdapter(recycledViewPool)
        vh.setItems(model.items)
    }

    override fun update(
        vh: WorkingDayViewHolder,
        model: WorkingDayItem,
        position: Int,
        payloads: List<Any>
    ) {
        vh.setItems(model.items)
    }
}

class WorkingDayAdapterItem(
    dayOfWeek: Int
) : AdapterItem<WorkingDayViewHolder, WorkingDayItem>(
    isType = { it is WorkingDayItem && it.dayOfWeek == dayOfWeek },
    layoutRes = R.layout.item_working_day,
    viewHolderGenerator = ::WorkingDayViewHolderImpl,
    itemBinder = WorkingDayItemBinder(
        recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 200) }
    ),
    areItemsTheSame = { a, b -> a.dayOfWeek == b.dayOfWeek },
    equals = { a, b -> a.items == b.items },
    changePayload = { a, b -> b.items }
)