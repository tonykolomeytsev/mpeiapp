package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

open class ShimmerItem(@LayoutRes val id: Int)

open class ShimmerAdapterItem(@LayoutRes layoutId: Int) : AdapterItem<ShimmerViewHolder, ShimmerItem>(
    isType = { it is ShimmerItem && it.id == layoutId },
    layoutRes = layoutId,
    viewHolderGenerator = ::ShimmerViewHolder,
    itemBinder = object : BaseItemBinder<ShimmerViewHolder, ShimmerItem>() {

        override fun bind(vh: ShimmerViewHolder, model: ShimmerItem, position: Int) {
            vh.startShimmer()
        }
    },
    areItemsTheSame = { a, b -> a.id == b.id }
)

class ShimmerViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    fun startShimmer() {
        (itemView as ShimmerFrameLayout).startShimmer()
    }
}
