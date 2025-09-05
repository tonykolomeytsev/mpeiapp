package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.views.scaleOnTouch
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemAddActionBinding

private const val ADD_ACTION_DEFAULT_ITEM_ID = 0
private const val ANIMATION_SCALE_MIN = 0.9f

public data class AddActionItem(
    val title: String,
    val itemId: Int = ADD_ACTION_DEFAULT_ITEM_ID
)

public class AddActionAdapterItem(
    isType: (Any) -> Boolean = { it is AddActionItem && it.itemId == ADD_ACTION_DEFAULT_ITEM_ID },
    onClickListener: ((AddActionItem) -> Unit)? = null
) : AdapterItem<AddActionViewHolder, AddActionItem>(
    isType = isType,
    layoutRes = R.layout.item_add_action,
    viewHolderGenerator = ::AddActionViewHolder,
    itemBinder = AddActionItemBinder(onClickListener)
)

public class AddActionViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)
{
    private val viewBinding = ItemAddActionBinding.bind(containerView)

    init {
        containerView.scaleOnTouch(factor = ANIMATION_SCALE_MIN)
    }

    public fun setTitle(title: String) {
        viewBinding.textViewActionName.text = title
    }
}

public class AddActionItemBinder(
    private val onClickListener: ((AddActionItem) -> Unit)?
) : BaseItemBinder<AddActionViewHolder, AddActionItem>() {

    override fun bind(vh: AddActionViewHolder, model: AddActionItem, position: Int) {
        vh.setOnClickListener { onClickListener?.invoke(model) }
        vh.setTitle(model.title)
    }
}
