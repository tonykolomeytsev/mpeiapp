package kekmech.ru.lib_schedule.items

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.ext_android.getStringArray
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.lib_schedule.R
import java.time.LocalDate
import kekmech.ru.coreui.R as coreui_R
import kekmech.ru.res_strings.R.array as StringArrays

public data class DayItem(
    val date: LocalDate,
    val weekOffset: Int,
    val isSelected: Boolean,
) {

    val dayOfWeek: Int get() = date.dayOfWeek.value

    public fun plusDays(int: Int): DayItem = copy(date = date.plusDays(int.toLong()))
}

public interface DayViewHolder {

    public var isSelected: Boolean

    public fun setDayOfWeekName(name: String)
    public fun setDayOfMonthNumber(number: Int)
    public fun setMonthName(name: String)
    public fun setIsCurrentDay(isCurrentDay: Boolean)
    public fun setIsSelected(isSelected: Boolean)
    public fun setOnClickListener(listener: (View) -> Unit)
}

public class DayViewHolderImpl(
    private val containerView: View,
) : RecyclerView.ViewHolder(containerView), DayViewHolder {

    private val textDayOfWeek = containerView.findViewById<TextView>(R.id.textDayOfWeek)
    private val textDayOfMonth = containerView.findViewById<TextView>(R.id.textDayOfMonth)
    private val textMonthName = containerView.findViewById<TextView>(R.id.textMonthName)
    private val viewMarker = containerView.findViewById<View>(R.id.viewMarker)
    private val viewBackground = containerView.findViewById<View>(R.id.viewBackground)
    override var isSelected: Boolean = false

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
            textDayOfMonth.setTextColor(context.getThemeColor(coreui_R.attr.colorWhite))
            textDayOfWeek.setTextColor(context.getThemeColor(coreui_R.attr.colorWhite))
            textMonthName.setTextColor(context.getThemeColor(coreui_R.attr.colorWhite))
        } else {
            textDayOfMonth.setTextColor(context.getThemeColor(coreui_R.attr.colorGray90))
            textDayOfWeek.setTextColor(context.getThemeColor(coreui_R.attr.colorGray90))
            textMonthName.setTextColor(context.getThemeColor(coreui_R.attr.colorGray70))
        }
    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        containerView.setOnClickListener(listener)
    }
}

public class DayItemBinder(
    context: Context,
    private val currentDate: LocalDate,
    private val onDayClickListener: (DayItem) -> Unit,
) : BaseItemBinder<DayViewHolder, DayItem>() {

    private val dayNames = context.getStringArray(StringArrays.days_of_week_short)
    private val monthNames = context.getStringArray(StringArrays.months_short)

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

public class DayAdapterItem(
    context: Context,
    onDayClickListener: (DayItem) -> Unit,
    layoutRes: Int = R.layout.item_day,
) : AdapterItem<DayViewHolder, DayItem>(
    isType = { it is DayItem },
    layoutRes = layoutRes,
    itemBinder = DayItemBinder(context, moscowLocalDate(), onDayClickListener),
    viewHolderGenerator = ::DayViewHolderImpl,
    areItemsTheSame = { a, b -> a.date == b.date },
    equals = { a, b -> a.date == b.date && a.isSelected == b.isSelected },
    changePayload = { _, b -> b } // if selection is not equals
)
