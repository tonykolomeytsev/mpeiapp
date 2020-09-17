package kekmech.ru.feature_schedule.main.item

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_day.*
import java.time.LocalDate

data class DayItem(
    val date: LocalDate,
    val weekOffset: Int,
    val isSelected: Boolean
) {
    val dayOfWeek get() = date.dayOfWeek.value

    fun plusDays(int: Int): DayItem = copy(date = date.plusDays(int.toLong()))
}

interface DayViewHolder {
    var isSelected: Boolean

    fun setDayOfWeekName(name: String)
    fun setDayOfMonthNumber(number: Int)
    fun setMonthName(name: String)
    fun setIsCurrentDay(isCurrentDay: Boolean)
    fun setIsSelected(isSelected: Boolean)
    fun setOnClickListener(listener: (View) -> Unit)
}

class DayViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), DayViewHolder, LayoutContainer {

    override var isSelected = false

    override fun setDayOfWeekName(name: String) {
        textDayOfWeek.text = name
    }

    override fun setDayOfMonthNumber(number: Int) {
        textDayOfMonth.text = number.toString()
    }

    override fun setMonthName(name: String) {
        textMonthName.text = name
    }

    override fun setIsCurrentDay(isCurrentDay: Boolean) {
        viewMarker.isVisible = isCurrentDay
    }

    override fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
        viewBackground.isVisible = isSelected
        val context = containerView.context
        if (isSelected) {
            textDayOfMonth.setTextColor(context.getThemeColor(R.attr.colorWhite))
            textDayOfWeek.setTextColor(context.getThemeColor(R.attr.colorWhite))
            textMonthName.setTextColor(context.getThemeColor(R.attr.colorWhite))
        } else {
            textDayOfMonth.setTextColor(context.getThemeColor(R.attr.colorGray90))
            textDayOfWeek.setTextColor(context.getThemeColor(R.attr.colorGray90))
            textMonthName.setTextColor(context.getThemeColor(R.attr.colorGray70))
        }
    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        linearLayoutContainer.setOnClickListener(listener)
    }
}

class DayItemBinder(
    context: Context,
    private val currentDate: LocalDate,
    private val onDayClickListener: (DayItem) -> Unit
) : BaseItemBinder<DayViewHolder, DayItem>() {

    private val dayNames = context.getStringArray(R.array.days_of_week_short)
    private val monthNames = context.getStringArray(R.array.months_short)

    override fun bind(vh: DayViewHolder, model: DayItem, position: Int) {
        val dayName = dayNames.getOrNull(model.date.dayOfWeek.value).orEmpty()
        val monthName = monthNames.getOrNull(model.date.month.value - 1).orEmpty()
        vh.setDayOfWeekName(dayName)
        vh.setDayOfMonthNumber(model.date.dayOfMonth)
        vh.setMonthName(monthName)
        vh.setOnClickListener { onDayClickListener(model) }
        vh.setIsCurrentDay(model.date == currentDate)
        vh.setIsSelected(model.isSelected)
    }

    override fun update(vh: DayViewHolder, model: DayItem, position: Int, payloads: List<Any>) {
        vh.setIsSelected(model.isSelected)
    }
}

class DayAdapterItem(
    context: Context,
    onDayClickListener: (DayItem) -> Unit
) : AdapterItem<DayViewHolder, DayItem>(
    isType = { it is DayItem },
    layoutRes = R.layout.item_day,
    itemBinder = DayItemBinder(context, LocalDate.now(), onDayClickListener),
    viewHolderGenerator = ::DayViewHolderImpl,
    areItemsTheSame = { a, b -> a.date == b.date },
    equals = { a, b -> a.date == b.date && a.isSelected == b.isSelected },
    changePayload = { _, b -> b } // if selection is not equals
)