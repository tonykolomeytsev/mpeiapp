package kekmech.ru.feature_schedule.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.helpers.DayItemStateHelper
import kekmech.ru.feature_schedule.main.item.WeekDaysViewHolderImpl
import java.time.LocalDate

class InfiniteScrollAdapter(
    private val localDatesGenerator: (weekOffset: Int) -> List<LocalDate>,
    private val onDayClickListener: (LocalDate) -> Unit
) : RecyclerView.Adapter<WeekDaysViewHolderImpl>() {
    private val infiniteScrollHelper = InfiniteScrollHelper()
    private var selectedDay: LocalDate = LocalDate.now()
    private val dayItemStateHelper = DayItemStateHelper()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        infiniteScrollHelper.attach(recyclerView)
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        infiniteScrollHelper.detach(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekDaysViewHolderImpl, position: Int) {
        val weekOffset = (Int.MAX_VALUE / 2) - position
        holder.setLocalDates(localDatesGenerator(-weekOffset))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDaysViewHolderImpl = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_week_days, parent, false)
        .let { view ->
            WeekDaysViewHolderImpl(view, { selectedDay }, { onDayClickListener(it) }, dayItemStateHelper)
        }

    fun selectDay(localDate: LocalDate) {
        selectedDay = localDate
    }
}