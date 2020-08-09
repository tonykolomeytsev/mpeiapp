package kekmech.ru.common_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter(
    vararg val adapterItems: AdapterItem<*, *>
) : ListAdapter<Any, RecyclerView.ViewHolder>(BaseDiffUtil(adapterItems)) {

    var allData: List<Any> = mutableListOf()
    var isLoading: Boolean = false

    open fun update(dataList: List<Any>) {
        allData = dataList
        submitList(dataList)
    }

    open fun move(fromPosition: Int, toPosition: Int) {
        val item = getItem(fromPosition)
        val newData = allData.toMutableList()
        newData.removeAt(fromPosition)
        newData.add(toPosition, item)
        update(newData)
    }

    open fun insert(dataList: List<Any>) {
        allData = allData + dataList
        submitList(allData)
    }

    override fun getItemCount() = getContentSize() + if (isLoading) 1 else 0

    private fun getContentSize() = super.getItemCount()

    override fun getItemViewType(position: Int): Int =
        if (isLoading && position == getContentSize()) {
            adapterItems.size
        } else {
            adapterItems.indexOfFirst { item -> item.isType(getItem(position)) }
                .takeIf { it != -1 }
                ?: error("Not registered AdapterItem for type ${getItem(position).javaClass.simpleName}")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        createItemViewHolder(parent, viewType)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isLoading && position == getContentSize()) return
        val data = getItem(position)
        val type = adapterItems.first { item -> item.isType(data) }
        type.itemBinder.bindView(holder, data, position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        if(isLoading && position == getContentSize()) return
        val data = getItem(position)
        val type = adapterItems.first { item -> item.isType(data) }
        type.itemBinder.updateViewHolder(holder, data, position, payloads)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        if (position == -1) return
        val type = adapterItems.first { item -> item.isType(getItem(position)) }
        type.itemBinder.detachViewHolder(holder)
        super.onViewDetachedFromWindow(holder)
    }

    fun getAdapterItemByPosition(position: Int): AdapterItem<*, *> =
        adapterItems[getItemViewType(position)]

    protected open fun createItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val adapterItem = adapterItems[viewType]
        return createItemView(parent, adapterItem.layoutRes)
            .let { view -> adapterItem.viewHolderGenerator(view) }
    }

    protected open fun createItemView(parent: ViewGroup, @LayoutRes layoutRes: Int): View =
        LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
}