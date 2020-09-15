package kekmech.ru.feature_schedule.main.item

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.helpers.WeeksScrollHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header.*
import java.time.LocalDate

data class HeaderItem(
    val currentWeekMonday: LocalDate,
    var selectedDay: LocalDate = LocalDate.now(),
    var selectedWeekNumber: Int = -1
)

interface HeaderViewHolder {
    fun setHeader(header: String)
    fun setDescription(description: String)
    fun bindHelper(helper: WeeksScrollHelper)
    fun unbindHelper(helper: WeeksScrollHelper)
    fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool)
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

    override fun setRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        recyclerView.setRecycledViewPool(recycledViewPool)
    }
}

class HeaderItemBinder(
    private val context: Context,
    onWeekSelectListener: (Int) -> Unit,
    onDaySelectListener: (DayItem) -> Unit
) : BaseItemBinder<HeaderViewHolder, HeaderItem>() {

    private val weeksScrollHelper =
        WeeksScrollHelper(
            onWeekSelectListener = onWeekSelectListener,
            onDayClickListener = onDaySelectListener
        )
    private val recycledViewPool = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(0, 20)
    }

    override fun bind(vh: HeaderViewHolder, model: HeaderItem, position: Int) {
        weeksScrollHelper.currentWeekMonday = model.currentWeekMonday
        vh.setRecycledViewPool(recycledViewPool)
        vh.setHeader(getFormattedDay(model.selectedDay))
        vh.bindHelper(weeksScrollHelper)
        vh.setDescription(getFormattedWeek(model.selectedWeekNumber))
    }

    override fun update(
        vh: HeaderViewHolder,
        model: HeaderItem,
        position: Int,
        payloads: List<Any>
    ) {
        val newModel = payloads.first() as HeaderItem
        vh.setHeader(getFormattedDay(newModel.selectedDay))
        vh.setDescription(getFormattedWeek(newModel.selectedWeekNumber))
    }

    private fun getFormattedDay(day: LocalDate): String {
        val dayOfWeekName = context.getStringArray(R.array.days_of_week).getOrElse(day.dayOfWeek.value - 1) { "" }
        val dayOfMonthName = context.getStringArray(R.array.months).getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $dayOfMonthName"
    }

    private fun getFormattedWeek(weekNumber: Int): String {
        if (weekNumber in 1..16) {
            return context.getString(R.string.schedule_semester_week, weekNumber)
        } else {
            return context.getString(R.string.schedule_weekend_week)
        }
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
    itemBinder = HeaderItemBinder(context, onWeekSelectListener, onDaySelectListener),
    areItemsTheSame = { a, b -> a.javaClass == b.javaClass },
    equals = { a, b -> a.selectedDay == b.selectedDay && a.selectedWeekNumber == b.selectedWeekNumber },
    changePayload = { _, b -> b }
)