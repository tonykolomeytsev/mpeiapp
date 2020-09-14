package kekmech.ru.feature_schedule.main.item

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.WeeksScrollHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header.*
import java.time.LocalDate

data class HeaderItem(
    val currentWeekMonday: LocalDate,
    var selectedDay: LocalDate = LocalDate.now(),
    var selectedWeekNumber: Int = -1
) {

    override fun equals(other: Any?) = other is HeaderItem
    override fun hashCode() = javaClass.hashCode()
}

interface HeaderViewHolder {
    fun setHeader(header: String)
    fun setDescription(description: String)
    fun bindHelper(helper: WeeksScrollHelper)
    fun unbindHelper(helper: WeeksScrollHelper)
}

class HeaderItemViewHolderImpl(
    override val containerView: View
) : HeaderViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var isHelperBinded = false

    override fun setHeader(header: String) {
        textViewHeader.text = header
    }

    override fun setDescription(description: String) {
        textViewDescription.text = description
    }

    override fun bindHelper(helper: WeeksScrollHelper) {
        if (!isHelperBinded) helper.attach(recyclerView)
        isHelperBinded = true
    }

    override fun unbindHelper(helper: WeeksScrollHelper) {
        if (isHelperBinded) helper.detach(recyclerView)
        isHelperBinded = false
    }
}

class HeaderItemBinder(
    private val context: Context,
    onWeekSelectListener: (Int) -> Unit,
    onDaySelectListener: (DayItem) -> Unit
) : BaseItemBinder<HeaderViewHolder, HeaderItem>() {

    private val weeksScrollHelper = WeeksScrollHelper(
        onWeekSelectListener = onWeekSelectListener,
        onDayClickListener = onDaySelectListener
    )

    override fun bind(vh: HeaderViewHolder, model: HeaderItem, position: Int) {
        weeksScrollHelper.currentWeekMonday = model.currentWeekMonday
        vh.setHeader(getFormattedDay(model.selectedDay))
        vh.bindHelper(weeksScrollHelper)
    }

    override fun update(
        vh: HeaderViewHolder,
        model: HeaderItem,
        position: Int,
        payloads: List<Any>
    ) {
        vh.setHeader(getFormattedDay(model.selectedDay))
    }

    private fun getFormattedDay(day: LocalDate): String {
        val dayOfWeekName = context.getStringArray(R.array.days_of_week).getOrElse(day.dayOfWeek.value - 1) { "" }
        val dayOfMonthName = context.getStringArray(R.array.months).getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $dayOfMonthName"
    }

}

class HeaderAdapterItem(
    context: Context,
    onWeekSelectListener: (Int) -> Unit,
    onDaySelectListener: (DayItem) -> Unit
) : AdapterItem<HeaderViewHolder, HeaderItem>(
    isType = { it is HeaderItem },
    viewHolderGenerator = ::HeaderItemViewHolderImpl,
    layoutRes = R.layout.item_header,
    itemBinder = HeaderItemBinder(context, onWeekSelectListener, onDaySelectListener)
)