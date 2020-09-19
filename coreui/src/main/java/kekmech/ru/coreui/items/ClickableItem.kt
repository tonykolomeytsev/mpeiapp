package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer

interface ClickableItemViewHolder {
    fun setOnClickListener(listener: (View) -> Unit)
}

class ClickableItemViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), ClickableItemViewHolder, LayoutContainer {

    override fun setOnClickListener(listener: (View) -> Unit) {
        containerView.setOnClickListener(listener)
    }
}

open class ClickableAdapterItem<T : Any>(
    isType: (Any) -> Boolean,
    @LayoutRes layoutRes: Int,
    onClickListener: ((T) -> Unit)? = null
) : AdapterItem<ClickableItemViewHolder, T>(
    isType = isType,
    layoutRes = layoutRes,
    viewHolderGenerator = ::ClickableItemViewHolderImpl,
    itemBinder = object : BaseItemBinder<ClickableItemViewHolder, T>() {
        override fun bind(vh: ClickableItemViewHolder, model: T, position: Int) {
            vh.setOnClickListener { onClickListener?.invoke(model) }
        }
    }
)
