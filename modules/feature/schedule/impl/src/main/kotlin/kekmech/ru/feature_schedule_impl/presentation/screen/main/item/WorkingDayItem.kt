package kekmech.ru.feature_schedule_impl.presentation.screen.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.ErrorStateAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.databinding.ItemWorkingDayBinding
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.lib_schedule.items.ClassesAdapterItem
import kekmech.ru.lib_schedule.items.LunchAdapterItem
import kekmech.ru.lib_schedule.items.NotePreviewAdapterItem
import kekmech.ru.lib_schedule.items.SelfStudyAdapterItem
import kekmech.ru.lib_schedule.items.WindowAdapterItem

const val DAY_OF_WEEK_MONDAY = 1
const val DAY_OF_WEEK_TUESDAY = 2
const val DAY_OF_WEEK_WEDNESDAY = 3
const val DAY_OF_WEEK_THURSDAY = 4
const val DAY_OF_WEEK_FRIDAY = 5
const val DAY_OF_WEEK_SATURDAY = 6

internal data class WorkingDayItem(
    val dayOfWeek: Int,
    val items: List<Any> = emptyList(),
)

internal class WorkingDayAdapterItem(
    dayOfWeek: Int,
    onClickListener: (Classes) -> Unit,
    onScrollClasses: (Int) -> Unit,
    onReloadClick: () -> Unit,
) : AdapterItem<WorkingDayViewHolder, WorkingDayItem>(
    isType = { it is WorkingDayItem && it.dayOfWeek == dayOfWeek },
    layoutRes = R.layout.item_working_day,
    viewHolderGenerator = ::WorkingDayViewHolder,
    itemBinder = WorkingDayItemBinder(
        recycledViewPool = RecyclerView.RecycledViewPool()
            .apply { setMaxRecycledViews(0, RECYCLED_VIEW_POOL_SIZE) },
        onClickListener = onClickListener,
        onScrollClasses = onScrollClasses,
        onReloadClick = onReloadClick
    ),
    areItemsTheSame = { a, b -> a.dayOfWeek == b.dayOfWeek },
    equals = { a, b -> a.items == b.items },
    changePayload = { _, b -> b.items }
) {

    companion object {

        const val RECYCLED_VIEW_POOL_SIZE = 200
    }
}

internal class WorkingDayViewHolder(
    private val containerView: View,
) : RecyclerView.ViewHolder(containerView) {

    private val adapter by fastLazy { createAdapter() }
    private val viewBinding = ItemWorkingDayBinding.bind(containerView)
    private var clickListener: (Classes) -> Unit = {}
    private var reloadClickListener: () -> Unit = {}

    init {
        viewBinding.recyclerView.adapter = adapter
    }

    fun update(list: List<Any>) {
        adapter.update(list)
    }

    fun setOnClickListener(listener: (Classes) -> Unit) {
        clickListener = listener
    }

    fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
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
        ShimmerAdapterItem(R.layout.item_working_day_shimmer),
        ErrorStateAdapterItem { reloadClickListener.invoke() }
    )

    fun addScrollListener(listener: (Int) -> Unit) {
        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                listener(dy)
            }
        })
    }

    fun setOnReloadClickListener(onReloadClick: () -> Unit) {
        reloadClickListener = onReloadClick
    }
}

internal class WorkingDayItemBinder(
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val onClickListener: (Classes) -> Unit,
    private val onScrollClasses: (Int) -> Unit,
    private val onReloadClick: () -> Unit,
) : BaseItemBinder<WorkingDayViewHolder, WorkingDayItem>() {

    override fun bind(vh: WorkingDayViewHolder, model: WorkingDayItem, position: Int) {
        vh.setRecycledViewPool(recycledViewPool)
        vh.setOnClickListener(onClickListener)
        vh.addScrollListener(onScrollClasses)
        vh.setOnReloadClickListener(onReloadClick)
        vh.update(model.items)
    }

    override fun update(
        vh: WorkingDayViewHolder,
        model: WorkingDayItem,
        position: Int,
        payloads: List<Any>,
    ) {
        vh.update(model.items)
    }
}
