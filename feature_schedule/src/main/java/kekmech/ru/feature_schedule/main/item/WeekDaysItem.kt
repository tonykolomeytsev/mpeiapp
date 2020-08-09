package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_days.*
import java.time.LocalDate

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
        adapter.update(localDates)
    }

    private fun createAdapter() = BaseAdapter(
        DayAdapterItem(containerView.context.getStringArray(R.array.days_of_week_short).asList())
    )
}