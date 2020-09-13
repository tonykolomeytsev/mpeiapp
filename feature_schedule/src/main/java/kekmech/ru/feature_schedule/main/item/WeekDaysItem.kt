package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.annotation.ArrayRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.helpers.DayItemStateHelper
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
    override val containerView: View,
    private val selectedDay: () -> LocalDate,
    private val dayClickListener: (LocalDate) -> Unit,
    private val helper: DayItemStateHelper
) : RecyclerView.ViewHolder(containerView), WeekDaysViewHolder, LayoutContainer {

    val adapter by fastLazy { createAdapter() }
    var items: List<DayItem> = emptyList()

    override fun setLocalDates(localDates: List<LocalDate>) {
        if (recyclerView.adapter == null) recyclerView.adapter = adapter
        if (recyclerView.layoutManager == null) recyclerView.layoutManager = GridLayoutManager(containerView.context, 6)
        items = localDates.map(::DayItem)
        adapter.update(items)
    }

    private fun createAdapter() = BaseAdapter(
        DayAdapterItem(
            extractStringList(R.array.days_of_week_short),
            extractStringList(R.array.months_short),
            selectedDay,
            dayClickListener,
            helper
        )
    )

    private fun extractStringList(@ArrayRes resId: Int) = containerView.context.getStringArray(resId).asList()
}

private fun LocalDate.weekOfYear() = get(WeekFields.of(Locale.GERMAN).weekOfWeekBasedYear())