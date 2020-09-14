package kekmech.ru.common_adapter

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtil(
    private val adapterItems: Array<out AdapterItem<*, *>>
) : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) return false
        val oldAdapterItem = oldItem.adapterItem
        if (oldAdapterItem != newItem.adapterItem) return false
        return oldAdapterItem.areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any) =
        oldItem.adapterItem.equals(oldItem, newItem)

    override fun getChangePayload(oldItem: Any, newItem: Any) =
        oldItem.adapterItem.changePayload(oldItem, newItem)

    @Suppress("UNCHECKED_CAST")
    private val Any.adapterItem get() =
        adapterItems.first { it.isType(this) } as AdapterItem<Any, Any>
}