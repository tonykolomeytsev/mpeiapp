package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.openLinkExternal
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_schedule.items.*
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.databinding.ItemWorkingDayBinding

const val DAY_OF_WEEK_MONDAY = 1
const val DAY_OF_WEEK_TUESDAY = 2
const val DAY_OF_WEEK_WEDNESDAY = 3
const val DAY_OF_WEEK_THURSDAY = 4
const val DAY_OF_WEEK_FRIDAY = 5
const val DAY_OF_WEEK_SATURDAY = 6

internal data class WorkingDayItem(
    val dayOfWeek: Int,
    val items: List<Any> = emptyList()
)

internal interface WorkingDayViewHolder {
    fun update(list: List<Any>)
    fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool)
    fun setOnClickListener(listener: (Classes) -> Unit)
    fun addScrollListener(listener: (Int) -> Unit)
}

internal class WorkingDayViewHolderImpl(
    private val containerView: View
) : WorkingDayViewHolder, RecyclerView.ViewHolder(containerView) {

    private val adapter by fastLazy { createAdapter() }
    private val viewBinding = ItemWorkingDayBinding.bind(containerView)
    private var clickListener: (Classes) -> Unit = {}

    init {
        viewBinding.recyclerView.adapter = adapter
    }

    override fun update(list: List<Any>) {
        adapter.update(list)
    }

    override fun setOnClickListener(listener: (Classes) -> Unit) {
        clickListener = listener
    }

    override fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        viewBinding.recyclerView.setRecycledViewPool(recycledViewPool)
    }

    private fun createAdapter() = BaseAdapter(
        ClassesAdapterItem(containerView.context, onClickListener = { clickListener(it) }),
        SelfStudyAdapterItem(),
        LunchAdapterItem { containerView.context.openLinkExternal("mpeix://map?tab=FOOD") },
        WindowAdapterItem(),
        EmptyStateAdapterItem(),
        SpaceAdapterItem(),
        NotePreviewAdapterItem(onClickListener = { clickListener(it) }),
        ShimmerAdapterItem(0, R.layout.item_working_day_shimmer)
    )

    override fun addScrollListener(listener: (Int) -> Unit) {
        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                listener(dy)
            }
        })
    }
}

internal class WorkingDayItemBinder(
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val onClickListener: (Classes) -> Unit,
    private val onScrollClasses: (Int) -> Unit
) : BaseItemBinder<WorkingDayViewHolder, WorkingDayItem>() {

    override fun bind(vh: WorkingDayViewHolder, model: WorkingDayItem, position: Int) {
        vh.setRecycledViewPool(recycledViewPool)
        vh.setOnClickListener(onClickListener)
        vh.addScrollListener(onScrollClasses)
        vh.update(model.items)
    }

    override fun update(
        vh: WorkingDayViewHolder,
        model: WorkingDayItem,
        position: Int,
        payloads: List<Any>
    ) {
        vh.update(model.items)
    }
}

internal class WorkingDayAdapterItem(
    dayOfWeek: Int,
    onClickListener: (Classes) -> Unit,
    onScrollClasses: (Int) -> Unit
) : AdapterItem<WorkingDayViewHolder, WorkingDayItem>(
    isType = { it is WorkingDayItem && it.dayOfWeek == dayOfWeek },
    layoutRes = R.layout.item_working_day,
    viewHolderGenerator = ::WorkingDayViewHolderImpl,
    itemBinder = WorkingDayItemBinder(
        recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, RECYCLED_VIEW_POOL_SIZE) },
        onClickListener = onClickListener,
        onScrollClasses = onScrollClasses
    ),
    areItemsTheSame = { a, b -> a.dayOfWeek == b.dayOfWeek },
    equals = { a, b -> a.items == b.items },
    changePayload = { _, b -> b.items }
) {
    companion object {
        const val RECYCLED_VIEW_POOL_SIZE = 200
    }
}