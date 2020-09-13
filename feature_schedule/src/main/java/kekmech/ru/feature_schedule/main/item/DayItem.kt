package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.helpers.DayItemStateHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_day.*
import java.time.LocalDate

data class DayItem(
    val date: LocalDate
)

interface DayViewHolder {
    var isSelected: Boolean

    fun setDayOfWeekName(name: String)
    fun setDayOfMonthNumber(number: Int)
    fun setMonthName(name: String)
    fun setIsCurrentDay(isCurrentDay: Boolean)
    fun setIsSelected(isSelected: Boolean)
    fun setOnClickListener(listener: () -> Unit)
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

    override fun setOnClickListener(listener: () -> Unit) {
        linearLayoutContainer.setOnClickListener { listener() }
    }
}

class DayItemBinder(
    private val dayNames: List<String>,
    private val monthNames: List<String>,
    private val currentDate: LocalDate,
    private val selectedDay: () -> LocalDate,
    private val dayClickListener: (LocalDate) -> Unit,
    private val helper: DayItemStateHelper
) : BaseItemBinder<DayViewHolder, DayItem>() {

    override fun bind(vh: DayViewHolder, model: DayItem, position: Int) {
        val dayName = dayNames.getOrNull(model.date.dayOfWeek.value).orEmpty()
        val monthName = monthNames.getOrNull(model.date.month.value - 1).orEmpty()
        vh.setDayOfWeekName(dayName)
        vh.setDayOfMonthNumber(model.date.dayOfMonth)
        vh.setMonthName(monthName)
        vh.setOnClickListener {
            dayClickListener(model.date)
            helper.clearOldSelectionAndRun {
                vh.setIsSelected(true)
            }
            helper.subscribeToClearSelection {
                vh.setIsSelected(false)
            }
        }
        if (model.date == selectedDay()) {
            vh.setIsSelected(true)
            helper.subscribeToClearSelection {
                vh.setIsSelected(false)
            }
        }
        if (model.date == currentDate) {
            vh.setIsCurrentDay(true)
        }
        val s = selectedDay()
        when {
            model.date == s && !vh.isSelected -> vh.setIsSelected(true)
            model.date != s && vh.isSelected -> vh.setIsSelected(false)
        }
    }
}

class DayAdapterItem(
    dayNames: List<String>,
    monthNames: List<String>,
    selectedDay: () -> LocalDate,
    dayClickListener: (LocalDate) -> Unit,
    helper: DayItemStateHelper
) : AdapterItem<DayViewHolder, DayItem>(
    isType = { it is DayItem },
    layoutRes = R.layout.item_day,
    itemBinder = DayItemBinder(dayNames, monthNames, LocalDate.now(), selectedDay, dayClickListener, helper),
    viewHolderGenerator = ::DayViewHolderImpl,
    areItemsTheSame = { a, b -> a.date == b.date },
    equals = { a, b -> a.date == b.date }
)