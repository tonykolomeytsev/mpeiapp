package kekmech.ru.coreui.items

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemAddActionBinding

private const val ADD_ACTION_DEFAULT_ITEM_ID = 0

data class AddActionItem(
    val title: String,
    val itemId: Int = ADD_ACTION_DEFAULT_ITEM_ID
)

interface AddActionViewHolder : ClickableItemViewHolder {
    fun setTitle(title: String)
}

@SuppressLint("ClickableViewAccessibility")
class AddActionViewHolderImpl(
    containerView: View
) :
    AddActionViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)
{
    private val viewBinding = ItemAddActionBinding.bind(containerView)

    init {
        containerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                containerView.clearAnimation()
                containerView.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100L)
                    .start()
            } else {
                containerView.clearAnimation()
                containerView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100L)
                    .start()
            }
            false
        }
    }

    override fun setTitle(title: String) {
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

class AddActionAdapterItem(
    isType: (Any) -> Boolean = { it is AddActionItem && it.itemId == ADD_ACTION_DEFAULT_ITEM_ID },
    onClickListener: ((AddActionItem) -> Unit)? = null
) : AdapterItem<AddActionViewHolder, AddActionItem>(
    isType = isType,
    layoutRes = R.layout.item_add_action,
    viewHolderGenerator = ::AddActionViewHolderImpl,
    itemBinder = AddActionItemBinder(onClickListener)
)