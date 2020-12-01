package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder

data class ShimmerItem(
    val id: Int
)

interface ShimmerViewHolder {
    fun startShimmer()
}

class ShimmerViewHolderImpl(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView), ShimmerViewHolder {

    override fun startShimmer() {
        (containerView as ShimmerFrameLayout).startShimmer()
    }
}

class ShimmerItemBinder : BaseItemBinder<ShimmerViewHolder, ShimmerItem>() {

    override fun bind(vh: ShimmerViewHolder, model: ShimmerItem, position: Int) {
        vh.startShimmer()
    }
}

open class ShimmerAdapterItem(id: Int, @LayoutRes layout: Int) : AdapterItem<ShimmerViewHolder, ShimmerItem>(
    isType = { it is ShimmerItem && it.id == id },
    layoutRes = layout,
    viewHolderGenerator = ::ShimmerViewHolderImpl,
    itemBinder = ShimmerItemBinder()
)