package kekmech.ru.lib_schedule.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.lib_schedule.R
import java.time.LocalTime

data class WindowItem(
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)

interface WindowViewHolder

internal class WindowViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), WindowViewHolder

internal class WindowItemBinder : BaseItemBinder<WindowViewHolder, WindowItem>() {

    override fun bind(vh: WindowViewHolder, model: WindowItem, position: Int) = Unit
}

class WindowAdapterItem : AdapterItem<WindowViewHolder, WindowItem>(
    isType = { it is WindowItem },
    layoutRes = R.layout.item_window,
    viewHolderGenerator = ::WindowViewHolderImpl,
    itemBinder = WindowItemBinder()
)
