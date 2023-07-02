package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemAddActionBinding
import kekmech.ru.ext_android.views.scaleOnTouch
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder

private const val ADD_ACTION_DEFAULT_ITEM_ID = 0
private const val ANIMATION_SCALE_MIN = 0.9f

data class AddActionItem(
    val title: String,
    val itemId: Int = ADD_ACTION_DEFAULT_ITEM_ID
)

class AddActionAdapterItem(
    isType: (Any) -> Boolean = { it is AddActionItem && it.itemId == ADD_ACTION_DEFAULT_ITEM_ID },
    onClickListener: ((AddActionItem) -> Unit)? = null
) : AdapterItem<AddActionViewHolder, AddActionItem>(
    isType = isType,
    layoutRes = R.layout.item_add_action,
    viewHolderGenerator = ::AddActionViewHolder,
    itemBinder = AddActionItemBinder(onClickListener)
)

class AddActionViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)
{
    private val viewBinding = ItemAddActionBinding.bind(containerView)

    init {
        containerView.scaleOnTouch(factor = ANIMATION_SCALE_MIN)
    }

    fun setTitle(title: String) {
        viewBinding.textViewActionName.text = title
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
