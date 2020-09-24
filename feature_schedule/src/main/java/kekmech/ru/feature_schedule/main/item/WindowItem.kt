package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_window.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class WindowItem(
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)

interface WindowViewHolder {
    fun setTimeStart(text: String)
    fun setTimeEnd(text: String)
}

class WindowViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), WindowViewHolder, LayoutContainer {

    override fun setTimeStart(text: String) {
        textViewTimeStart.text = text
    }

    override fun setTimeEnd(text: String) {
        textViewTimeEnd.text = text
    }
}

class WindowItemBinder : BaseItemBinder<WindowViewHolder, WindowItem>() {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun bind(vh: WindowViewHolder, model: WindowItem, position: Int) {
        vh.setTimeStart(model.timeStart.format(timeFormatter))
        vh.setTimeEnd(model.timeEnd.format(timeFormatter))
    }
}

class WindowAdapterItem : AdapterItem<WindowViewHolder, WindowItem>(
    isType = { it is WindowItem },
    layoutRes = R.layout.item_window,
    viewHolderGenerator = ::WindowViewHolderImpl,
    itemBinder = WindowItemBinder()
)