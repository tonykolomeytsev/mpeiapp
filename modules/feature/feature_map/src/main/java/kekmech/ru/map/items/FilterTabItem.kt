package kekmech.ru.map.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.map.R
import kekmech.ru.map.databinding.ItemFilterTabBinding
import kekmech.ru.map.elm.FilterTab

internal data class FilterTabItem(
    @DrawableRes val drawableResId: Int,
    @StringRes val nameResId: Int,
    val tab: FilterTab
)

internal class FilterTabAdapterItem(
    onClickListener: ((FilterTab) -> Unit)? = null
) : AdapterItem<FilterTabViewHolder, FilterTabItem>(
    isType = { it is FilterTabItem },
    layoutRes = R.layout.item_filter_tab,
    viewHolderGenerator = ::FilterTabViewHolder,
    itemBinder = FilterTabItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.tab == b.tab }
)

internal class FilterTabViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemFilterTabBinding.bind(containerView)

    fun setName(@StringRes nameResId: Int) {
        viewBinding.textViewName.setText(nameResId)
    }

    fun setIcon(@DrawableRes drawableResId: Int) {
        viewBinding.imageViewIcon.setImageResource(drawableResId)
    }
}

private class FilterTabItemBinder(
    private val onClickListener: ((FilterTab) -> Unit)?
) : BaseItemBinder<FilterTabViewHolder, FilterTabItem>() {

    override fun bind(vh: FilterTabViewHolder, model: FilterTabItem, position: Int) {
        vh.setName(model.nameResId)
        vh.setIcon(model.drawableResId)
        vh.setOnClickListener { onClickListener?.invoke(model.tab) }
    }
}
