package kekmech.ru.coreui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemSpaceBinding
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

public data class SpaceItem(
    val width: Int,
    val height: Int
) {

    public companion object {
        public val VERTICAL_4: SpaceItem = SpaceItem(0, 4)
        public val VERTICAL_8: SpaceItem = SpaceItem(0, 8)
        public val VERTICAL_12: SpaceItem = SpaceItem(0, 12)
        public val VERTICAL_16: SpaceItem = SpaceItem(0, 16)
        public val VERTICAL_24: SpaceItem = SpaceItem(0, 24)
    }
}

public class SpaceAdapterItem : AdapterItem<SpaceViewHolder, SpaceItem>(
    isType = { it is SpaceItem },
    layoutRes = R.layout.item_space,
    viewHolderGenerator = ::SpaceViewHolder,
    itemBinder = SpaceItemBinder()
)

public class SpaceViewHolder(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemSpaceBinding.bind(containerView)

    public fun setWidth(width: Int) {
        viewBinding.space.layoutParams.width = containerView.context.resources.dpToPx(width)
    }

    public fun setHeight(height: Int) {
        viewBinding.space.layoutParams.height = containerView.context.resources.dpToPx(height)
    }
}

public class SpaceItemBinder : BaseItemBinder<SpaceViewHolder, SpaceItem>() {

    override fun bind(vh: SpaceViewHolder, model: SpaceItem, position: Int) {
        vh.setWidth(model.width)
        vh.setHeight(model.height)
    }
}
