package kekmech.ru.library_schedule.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder
import kekmech.ru.library_schedule.R

object LunchItem

interface LunchViewHolder : ClickableItemViewHolder

private class LunchViewHolderImpl(
    containerView: View
) : LunchViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)

private class LunchItemBinder(
    private val onClickListener: () -> Unit
) : BaseItemBinder<LunchViewHolder, LunchItem>() {

    override fun bind(vh: LunchViewHolder, model: LunchItem, position: Int) {
        vh.setOnClickListener { onClickListener() }
    }
}

class LunchAdapterItem(
    onClickListener: () -> Unit
) : AdapterItem<LunchViewHolder, LunchItem>(
    isType = { it is LunchItem },
    layoutRes = R.layout.item_lunch,
    viewHolderGenerator = ::LunchViewHolderImpl,
    itemBinder = LunchItemBinder(onClickListener)
)
