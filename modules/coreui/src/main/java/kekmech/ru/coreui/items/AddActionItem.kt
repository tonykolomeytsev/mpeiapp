package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_add_action.*

private const val ADD_ACTION_DEFAULT_ITEM_ID = 0

data class AddActionItem(
    val title: String,
    val itemId: Int = ADD_ACTION_DEFAULT_ITEM_ID
)

interface AddActionViewHolder : ClickableItemViewHolder {
    fun setTitle(title: String)
}

class AddActionViewHolderImpl(
    override val containerView: View
) :
    AddActionViewHolder,
    RecyclerView.ViewHolder(containerView),
    LayoutContainer,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)
{

    override fun setTitle(title: String) {
        textViewActionName.text = title
    }
}

class AddActionItemBinder(
    private val onClickListener: ((AddActionItem) -> Unit)?
) : BaseItemBinder<AddActionViewHolder, AddActionItem>() {

    override fun bind(vh: AddActionViewHolder, model: AddActionItem, position: Int) {
        vh.setOnClickListener { onClickListener?.invoke(model) }
        vh.setTitle(model.title)
    }
}

class AddActionAdapterItem(
    isType: (Any) -> Boolean = { it is AddActionItem && it.itemId == ADD_ACTION_DEFAULT_ITEM_ID },
    onClickListener: ((AddActionItem) -> Unit)? = null
) : AdapterItem<AddActionViewHolder, AddActionItem>(
    isType = isType,
    layoutRes = R.layout.item_add_action,
    viewHolderGenerator = ::AddActionViewHolderImpl,
    itemBinder = AddActionItemBinder(onClickListener)
)