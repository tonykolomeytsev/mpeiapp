package kekmech.ru.map.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.map.R
import kekmech.ru.map.databinding.ItemTabBarBinding
import kekmech.ru.map.elm.FilterTab

internal object TabBarItem

internal interface TabBarViewHolder {
    fun createAdapterIfNull()
    fun updateItems(items: List<Any>)
    fun setOnClickListener(listener: (FilterTab) -> Unit)
}

internal class TabBarViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), TabBarViewHolder {

    private var adapter: BaseAdapter? = null
    private var onClickListener: ((FilterTab) -> Unit)? = null
    private val viewBinding = ItemTabBarBinding.bind(containerView)

    override fun createAdapterIfNull() {
        if (adapter == null) {
            adapter = BaseAdapter(
                FilterTabAdapterItem { onClickListener?.invoke(it) }
            )
            with(viewBinding) {
                recyclerView.adapter = adapter
                recyclerView.layoutManager =
                    LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    override fun updateItems(items: List<Any>) {
        adapter?.update(items)
    }

    override fun setOnClickListener(listener: (FilterTab) -> Unit) {
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

internal class TabBarAdapterItem(
    tabs: List<FilterTabItem>,
    onClickListener: ((FilterTab) -> Unit)?
) : AdapterItem<TabBarViewHolder, TabBarItem>(
    isType = { it is TabBarItem },
    layoutRes = R.layout.item_tab_bar,
    viewHolderGenerator = ::TabBarViewHolderImpl,
    itemBinder = TabBarItemBinder(tabs, onClickListener)
)