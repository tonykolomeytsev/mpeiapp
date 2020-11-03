package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer
import java.time.LocalTime

internal data class WindowItem(
    val timeStart: LocalTime,
    val timeEnd: LocalTime
)

internal interface WindowViewHolder

internal class WindowViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), WindowViewHolder, LayoutContainer

internal class WindowItemBinder : BaseItemBinder<WindowViewHolder, WindowItem>() {

    override fun bind(vh: WindowViewHolder, model: WindowItem, position: Int) = Unit
}

internal class WindowAdapterItem : AdapterItem<WindowViewHolder, WindowItem>(
    isType = { it is WindowItem },
    layoutRes = R.layout.item_window,
    viewHolderGenerator = ::WindowViewHolderImpl,
    itemBinder = WindowItemBinder()
)