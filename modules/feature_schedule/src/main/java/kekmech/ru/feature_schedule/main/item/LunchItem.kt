package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_schedule.R

internal object LunchItem

internal interface LunchViewHolder : ClickableItemViewHolder

internal class LunchViewHolderImpl(
    containerView: View
) : LunchViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)

internal class LunchItemBinder(
    private val onClickListener: () -> Unit
) : BaseItemBinder<LunchViewHolder, LunchItem>() {

    override fun bind(vh: LunchViewHolder, model: LunchItem, position: Int) {
        vh.setOnClickListener { onClickListener() }
    }
}

internal class LunchAdapterItem(
    onClickListener: () -> Unit
) : AdapterItem<LunchViewHolder, LunchItem>(
    isType = { it is LunchItem },
    layoutRes = R.layout.item_lunch,
    viewHolderGenerator = ::LunchViewHolderImpl,
    itemBinder = LunchItemBinder(onClickListener)
)