package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_space.*

data class SpaceItem(
    val width: Int,
    val height: Int
) {

    companion object {
        val VERTICAL_8 = SpaceItem(0, 8)
        val VERTICAL_12 = SpaceItem(0, 12)
        val VERTICAL_16 = SpaceItem(0, 16)
        val VERTICAL_24 = SpaceItem(0, 24)
    }
}

interface SpaceViewHolder {
    fun setWidth(width: Int)
    fun setHeight(height: Int)
}

class SpaceViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), SpaceViewHolder, LayoutContainer {

    override fun setWidth(width: Int) {
        space.layoutParams.width = containerView.context.resources.dpToPx(width)
    }

    override fun setHeight(height: Int) {
        space.layoutParams.height = containerView.context.resources.dpToPx(height)
    }
}

class SpaceItemBinder : BaseItemBinder<SpaceViewHolder, SpaceItem>() {

    override fun bind(vh: SpaceViewHolder, model: SpaceItem, position: Int) {
        vh.setWidth(model.width)
        vh.setHeight(model.height)
    }
}

class SpaceAdapterItem : AdapterItem<SpaceViewHolder, SpaceItem>(
    isType = { it is SpaceItem },
    layoutRes = R.layout.item_space,
    viewHolderGenerator = ::SpaceViewHolderImpl,
    itemBinder = SpaceItemBinder()
)