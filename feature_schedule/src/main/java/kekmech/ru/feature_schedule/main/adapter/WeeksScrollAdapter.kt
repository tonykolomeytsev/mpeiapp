package kekmech.ru.feature_schedule.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.helpers.WeeksScrollHelper
import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.item.WeekItem
import kekmech.ru.feature_schedule.main.item.WeekItemBinder
import kekmech.ru.feature_schedule.main.item.WeekViewHolderImpl
import java.time.LocalDate
import java.util.*

class WeeksScrollAdapter(
    private val localDatesGenerator: WeeksScrollHelper.LocalDatesGenerator
) : RecyclerView.Adapter<WeekViewHolderImpl>() {
    private var onDayClickListener: (DayItem) -> Unit = {}

    private val weakWeekItems = WeakHashMap<Int, WeekItem>()
    val items: List<WeekItem> get() = weakWeekItems.map { (_, v) -> v }
    private val recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 200) }
    private val weekItemBinder = WeekItemBinder(recycledViewPool) { onDayClickListener(it) }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekViewHolderImpl, position: Int) {
        val weekOffset = -((Int.MAX_VALUE / 2) - position)
        val weekItem = weakWeekItems.getOrPut(weekOffset) { createWeekItem(weekOffset, localDatesGenerator(weekOffset)) }
        weekItemBinder.bind(holder, weekItem, weekOffset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolderImpl = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_week_days, parent, false)
        .let(::WeekViewHolderImpl)

    fun setOnDayClickListener(listener: (DayItem) -> Unit) {
        onDayClickListener = listener
    }

    private fun createWeekItem(
        weekOffset: Int,
        daysDates: List<LocalDate>
    ) = WeekItem(
        weekOffset = weekOffset,
        firstDayOfWeek = daysDates.first(),
        dayItems = daysDates.map(::DayItem)
    )
}