package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_days.*
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

data class WeekDaysItem(
    val firstDayOfWeek: LocalDate
)

interface WeekDaysViewHolder {
    fun setLocalDates(localDates: List<LocalDate>)
}

class WeekDaysViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), WeekDaysViewHolder, LayoutContainer {

    val adapter by fastLazy { createAdapter() }

    override fun setLocalDates(localDates: List<LocalDate>) {
        if (recyclerView.adapter == null) recyclerView.adapter = adapter
        if (recyclerView.layoutManager == null) recyclerView.layoutManager = GridLayoutManager(containerView.context, 6)
        adapter.update(localDates.map(::DayItem))
    }

    private fun createAdapter() = BaseAdapter(
        DayAdapterItem(containerView.context.getStringArray(R.array.days_of_week_short).asList())
    )
}

class WeekDaysItemBinder : BaseItemBinder<WeekDaysViewHolder, WeekDaysItem>() {

    override fun bind(vh: WeekDaysViewHolder, model: WeekDaysItem, position: Int) {
        // monday + tuesday..saturday
        val weekDaysDates = listOf(model.firstDayOfWeek, *Array(5) { model.firstDayOfWeek.plusDays(it + 1L) })
        vh.setLocalDates(weekDaysDates)
    }
}

class WeekDaysAdapterItem : AdapterItem<WeekDaysViewHolder, WeekDaysItem>(
    isType = { it is WeekDaysItem },
    layoutRes = R.layout.item_week_days,
    viewHolderGenerator = ::WeekDaysViewHolderImpl,
    itemBinder = WeekDaysItemBinder(),
    areItemsTheSame = { first, second -> first.firstDayOfWeek.weekOfYear() == second.firstDayOfWeek.weekOfYear() },
    equals = { first, second -> first.firstDayOfWeek.weekOfYear() == second.firstDayOfWeek.weekOfYear() }
)

private fun LocalDate.weekOfYear() = get(WeekFields.of(Locale.GERMAN).weekOfWeekBasedYear())