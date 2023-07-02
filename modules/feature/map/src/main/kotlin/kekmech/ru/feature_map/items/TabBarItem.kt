package kekmech.ru.feature_map.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_map.R
import kekmech.ru.feature_map.databinding.ItemTabBarBinding
import kekmech.ru.feature_map.screens.main.elm.FilterTab
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseAdapter
import kekmech.ru.library_adapter.BaseItemBinder

internal object TabBarItem

internal class TabBarAdapterItem(
    tabs: List<FilterTabItem>,
    onClickListener: ((FilterTab) -> Unit)?
) : AdapterItem<TabBarViewHolder, TabBarItem>(
    isType = { it is TabBarItem },
    layoutRes = R.layout.item_tab_bar,
    viewHolderGenerator = ::TabBarViewHolder,
    itemBinder = TabBarItemBinder(tabs, onClickListener)
)

internal class TabBarViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private var adapter: BaseAdapter? = null
    private var onClickListener: ((FilterTab) -> Unit)? = null
    private val viewBinding = ItemTabBarBinding.bind(containerView)

    fun createAdapterIfNull() {
        if (adapter == null) {
            adapter = BaseAdapter(
                FilterTabAdapterItem { onClickListener?.invoke(it) }
            )
            with(viewBinding) {
                recyclerView.adapter = adapter
            }
        }
    }

    fun updateItems(items: List<Any>) {
        adapter?.update(items)
    }

    fun setOnClickListener(listener: (FilterTab) -> Unit) {
        onClickListener = listener
    }
}

internal class TabBarItemBinder(
    private val tabs: List<FilterTabItem>,
    private val onClickListener: ((FilterTab) -> Unit)?
) : BaseItemBinder<TabBarViewHolder, TabBarItem>() {

    override fun bind(vh: TabBarViewHolder, model: TabBarItem, position: Int) {
        vh.createAdapterIfNull()
        vh.updateItems(tabs)
        onClickListener?.let(vh::setOnClickListener)
    }
}
