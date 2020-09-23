package kekmech.ru.map.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.map.R
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.map.presentation.FilterTab
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_filter_tab.*

data class FilterTabItem(
    @DrawableRes val drawableResId: Int,
    @StringRes val nameResId: Int,
    val tab: FilterTab
)

interface FilterTabViewHolder : ClickableItemViewHolder {
    fun setName(@StringRes nameResId: Int)
    fun setIcon(@DrawableRes drawableResId: Int)
}

class FilterTabViewHolderImpl(
    override val containerView: View
) :
    FilterTabViewHolder,
    RecyclerView.ViewHolder(containerView),
    LayoutContainer,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    override fun setName(@StringRes nameResId: Int) {
        textViewName.setText(nameResId)
    }

    override fun setIcon(@DrawableRes drawableResId: Int) {
        imageViewIcon.setImageResource(drawableResId)
    }
}

class FilterTabItemBinder(
    private val onClickListener: ((FilterTab) -> Unit)?
) : BaseItemBinder<FilterTabViewHolder, FilterTabItem>() {

    override fun bind(vh: FilterTabViewHolder, model: FilterTabItem, position: Int) {
        vh.setName(model.nameResId)
        vh.setIcon(model.drawableResId)
        vh.setOnClickListener { onClickListener?.invoke(model.tab) }
    }
}

class FilterTabAdapterItem(
    onClickListener: ((FilterTab) -> Unit)? = null
) : AdapterItem<FilterTabViewHolder, FilterTabItem>(
    isType = { it is FilterTabItem },
    layoutRes = R.layout.item_filter_tab,
    viewHolderGenerator = ::FilterTabViewHolderImpl,
    itemBinder = FilterTabItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.tab == b.tab }
)