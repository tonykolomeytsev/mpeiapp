package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer

private const val DAY_SHIMMER_ITEM = 0
private const val WEEK_SHIMMER_ITEM = 1
private const val HEADER_SHIMMER_ITEM = 2
private const val CLASSES_SHIMMER_ITEM = 3

data class ShimmerItem(
    val id: Int
) {
    companion object {
        fun header() = ShimmerItem(HEADER_SHIMMER_ITEM)
        fun classes() = ShimmerItem(CLASSES_SHIMMER_ITEM)
    }
}

interface ShimmerViewHolder {
    fun startShimmer()
}

class ShimmerViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), ShimmerViewHolder, LayoutContainer {

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

class ClassesShimmerAdapterItem : ShimmerAdapterItem(CLASSES_SHIMMER_ITEM, R.layout.item_working_day_shimmer)
