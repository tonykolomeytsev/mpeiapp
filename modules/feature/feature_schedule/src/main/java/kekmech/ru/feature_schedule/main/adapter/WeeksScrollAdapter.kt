package kekmech.ru.feature_schedule.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.utils.atStartOfWeek
import kekmech.ru.feature_schedule.main.item.WeekAdapterItem
import kekmech.ru.feature_schedule.main.item.WeekItem
import kekmech.ru.feature_schedule.main.item.WeekViewHolder
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val HALF_INT_MAX_VALUE = Int.MAX_VALUE / 2
private const val DAYS_IN_CALENDAR = 6

internal class WeeksScrollAdapter(
    private val weekAdapterItem: WeekAdapterItem
) : RecyclerView.Adapter<WeekViewHolder>() {

    private val currentDate: LocalDate = moscowLocalDate()
    private val nearestPastMonday: LocalDate = currentDate.atStartOfWeek()
    private var lastSelectedDate: LocalDate = currentDate
    private var lastSelectedWeekOffset: Int = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.scrollToPosition(HALF_INT_MAX_VALUE)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val weekOffset = position - HALF_INT_MAX_VALUE
        weekAdapterItem.itemBinder.bind(holder, createWeekItemFor(weekOffset), weekOffset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder = LayoutInflater
        .from(parent.context)
        .inflate(weekAdapterItem.layoutRes, parent, false)
        .let(weekAdapterItem.viewHolderGenerator) as WeekViewHolder

    private fun createWeekItemFor(weekOffset: Int): WeekItem {
        val actualMonday = nearestPastMonday.plusWeeks(weekOffset.toLong())
        return WeekItem(
            weekOffset = weekOffset,
            firstDayOfWeek = actualMonday,
            dayItems = List(DAYS_IN_CALENDAR) { offset ->
                val dayDate = actualMonday.plusDays(offset.toLong())
                DayItem(dayDate, weekOffset, lastSelectedDate == dayDate)
            }
        )
    }

    fun selectDay(selectedDate: LocalDate, needToScrollListener: (position: Int) -> Unit) {
        notifyItemChanged(lastSelectedWeekOffset + HALF_INT_MAX_VALUE)
        val weekDistance = lastSelectedDate.atStartOfWeek()
            .until(selectedDate.atStartOfWeek(), ChronoUnit.WEEKS).toInt()
        lastSelectedWeekOffset += weekDistance
        lastSelectedDate = selectedDate
        notifyItemChanged(lastSelectedWeekOffset + HALF_INT_MAX_VALUE)
        needToScrollListener(lastSelectedWeekOffset + HALF_INT_MAX_VALUE)
    }
}