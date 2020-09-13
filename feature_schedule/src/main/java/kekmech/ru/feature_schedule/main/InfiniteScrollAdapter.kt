package kekmech.ru.feature_schedule.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.item.WeekDaysViewHolderImpl
import java.time.LocalDate

class InfiniteScrollAdapter(
    private val localDatesGenerator: (weekOffset: Int) -> List<LocalDate>
) : RecyclerView.Adapter<WeekDaysViewHolderImpl>() {
    private val infiniteScrollHelper = InfiniteScrollHelper()
    private var startBorder = 0
    private var endBorder = 0
    private var onStartReached: (startBorder: Int) -> Unit = {}
    private var onEndReached: (endBorder: Int) -> Unit = {}

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        infiniteScrollHelper.attach(recyclerView)
        infiniteScrollHelper.setOnStartReached { onStartReached(++startBorder) }
        infiniteScrollHelper.setOnEndReached { onEndReached(++endBorder) }
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        infiniteScrollHelper.detach(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun setOnStartReached(listener: (startBorder: Int) -> Unit) { onStartReached = listener }

    fun setOnEndReached(listener: (endBorder: Int) -> Unit) { onEndReached = listener }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekDaysViewHolderImpl, position: Int) {
        val weekOffset = (Int.MAX_VALUE / 2) - position
        holder.setLocalDates(localDatesGenerator(-weekOffset))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDaysViewHolderImpl =
        LayoutInflater.from(parent.context).inflate(R.layout.item_week_days, parent, false).let(::WeekDaysViewHolderImpl)
}