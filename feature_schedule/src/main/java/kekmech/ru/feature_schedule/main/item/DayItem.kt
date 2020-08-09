package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_day.*
import java.time.LocalDate

data class DayItem(
    val date: LocalDate
)

interface DayViewHolder {
    fun setDayOfWeekName(name: String)
    fun setDayOfMonthNumber(number: Int)
}

class DayViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), DayViewHolder, LayoutContainer {

    override fun setDayOfWeekName(name: String) {
        textDayOfWeek.text = name
    }

    override fun setDayOfMonthNumber(number: Int) {
        textDayOfMonth.text = number.toString()
    }
}

class DayItemBinder(
    private val dayNames: List<String>
) : BaseItemBinder<DayViewHolder, DayItem>() {

    override fun bind(vh: DayViewHolder, model: DayItem, position: Int) {
        val dayName = dayNames.getOrNull(model.date.dayOfWeek.value).orEmpty()
        vh.setDayOfWeekName(dayName)
        vh.setDayOfMonthNumber(model.date.dayOfMonth)
    }
}

class DayAdapterItem(dayNames: List<String>) : AdapterItem<DayViewHolder, DayItem>(
    isType = { it is DayItem },
    layoutRes = R.layout.item_day,
    itemBinder = DayItemBinder(dayNames),
    viewHolderGenerator = ::DayViewHolderImpl
)