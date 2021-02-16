package kekmech.ru.feature_schedule.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.items.WeekAdapterItem
import kekmech.ru.common_schedule.items.WeekItem
import kekmech.ru.common_schedule.items.WeekViewHolderImpl

internal class WeeksScrollAdapter(
    private val weekAdapterItem: WeekAdapterItem
) : RecyclerView.Adapter<WeekViewHolderImpl>() {

    private val allData = HashMap<Int, WeekItem>()
    private var lastSelectedDayItem: DayItem? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekViewHolderImpl, position: Int) {
        val weekOffset = position - (Int.MAX_VALUE / 2)
        val weekItem = allData[weekOffset] ?: return
        weekAdapterItem.itemBinder.bind(holder, weekItem, weekOffset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolderImpl = LayoutInflater
        .from(parent.context)
        .inflate(weekAdapterItem.layoutRes, parent, false)
        .let(weekAdapterItem.viewHolderGenerator) as WeekViewHolderImpl

    /**
     *
     * @param newData - map of {weekOffset => WeekItem}
     * @param forceUpdateItems - list of weekOffset to update
     */
    fun update(newData: HashMap<Int, WeekItem>) {
        val changedKeys = newData.keys - allData.keys
        allData.putAll(newData)
        changedKeys.forEach { key ->
            notifyItemChanged(key.toAdapterPosition())
        }
    }

    fun selectDay(selectedDayItem: DayItem) {
        if (lastSelectedDayItem?.weekOffset == selectedDayItem.weekOffset) {
            lastSelectedDayItem = selectedDayItem
            modifyWeekItem(selectedDayItem.weekOffset) { withSelectedDay(selectedDayItem) }
            notifyItemChanged(selectedDayItem.weekOffset.toAdapterPosition())
        } else {
            lastSelectedDayItem?.let { dayItem ->
                modifyWeekItem(dayItem.weekOffset) { withSelectedDay(dayItem, false) }
                notifyItemChanged(dayItem.weekOffset.toAdapterPosition())
            }
            lastSelectedDayItem = selectedDayItem
            modifyWeekItem(selectedDayItem.weekOffset) { withSelectedDay(selectedDayItem) }
            notifyItemChanged(selectedDayItem.weekOffset.toAdapterPosition())
        }
    }

    private fun modifyWeekItem(weekOffset: Int, modifier: WeekItem.() -> WeekItem) {
        allData[weekOffset]?.modifier()?.let { allData[weekOffset] = it }
    }

    private fun WeekItem.withSelectedDay(dayItem: DayItem, forceValue: Boolean? = null): WeekItem =
        copy(dayItems = dayItems.map { it.copy(isSelected = forceValue ?: it.date == dayItem.date) })

    private fun Int.toAdapterPosition(): Int = plus(Int.MAX_VALUE / 2)
}