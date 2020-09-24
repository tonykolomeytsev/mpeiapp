package kekmech.ru.feature_dashboard.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_dashboard.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_change_group.*

data class ChangeGroupItem(
    val groupName: String
)

interface ChangeGroupViewHolder : ClickableItemViewHolder {
    fun setName(name: String)
}

class ChangeGroupViewHolderImpl(
    override val containerView: View
) :
    ChangeGroupViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    override fun setName(name: String) {
        textViewGroupName.text = containerView.context.getString(R.string.dashboard_item_change_group, name)
    }
}

class ChangeGroupItemBinder(
    private val onClickListener: ((ChangeGroupItem) -> Unit)?
) : BaseItemBinder<ChangeGroupViewHolder, ChangeGroupItem>() {

    override fun bind(vh: ChangeGroupViewHolder, model: ChangeGroupItem, position: Int) {
        vh.setName(model.groupName)
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}

class ChangeGroupAdapterItem(
    onClickListener: ((ChangeGroupItem) -> Unit)? = null
) : AdapterItem<ChangeGroupViewHolder, ChangeGroupItem>(
    isType = { it is ChangeGroupItem },
    layoutRes = R.layout.item_change_group,
    viewHolderGenerator = ::ChangeGroupViewHolderImpl,
    itemBinder = ChangeGroupItemBinder(onClickListener)
)