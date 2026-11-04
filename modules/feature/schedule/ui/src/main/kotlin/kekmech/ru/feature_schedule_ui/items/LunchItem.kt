package kekmech.ru.feature_schedule_ui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_schedule_ui.R
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

public object LunchItem

public interface LunchViewHolder : ClickableItemViewHolder

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

public class LunchAdapterItem(
    onClickListener: () -> Unit
) : AdapterItem<LunchViewHolder, LunchItem>(
    isType = { it is LunchItem },
    layoutRes = R.layout.item_lunch,
    viewHolderGenerator = ::LunchViewHolderImpl,
    itemBinder = LunchItemBinder(onClickListener)
)
