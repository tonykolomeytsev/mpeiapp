package kekmech.ru.coreui.deprecated.adapter

import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

open class BaseSortedAdapter : BaseAdapter() {

    private var order: Map<Class<out BaseItem<*>>, Int> = emptyMap()
    private var isAllowedOnlyUniqueItems: Boolean = false

    override fun addItem(baseItem: BaseItem<*>) {
        fun index(item: BaseItem<*>) = order[item::class.java]
        if (!isAllowedOnlyUniqueItems) {
            items += baseItem
            items.sortBy(::index)
            notifyItemInserted(items.indexOf(baseItem))
        } else {
            val similarItem = items.firstOrNull { it.javaClass == baseItem.javaClass }
            if (similarItem != null) {
                val index = items.indexOf(similarItem)
                items[index] = baseItem
                notifyItemChanged(index)
            } else {
                items += baseItem
                items.sortBy(::index)
                notifyItemInserted(items.indexOf(baseItem))
            }
        }
    }

    fun removeItemByClass(kClass: KClass<out BaseItem<*>>) {
        val index = items.indexOfFirst { it.javaClass == kClass.java }
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemId(position: Int): Long {
        return order[items[position]::class.java]?.toLong() ?: RecyclerView.NO_ID
    }

    class Builder {
        private val adapter = BaseSortedAdapter()

        fun registerViewTypeFactory(factory: BaseFactory): Builder {
            adapter.viewFactories += adapter.viewFactories.size to factory
            return this
        }

        fun addItemsOrder(list: List<KClass<out BaseItem<*>>>): Builder {
            adapter.order = list.mapIndexed { i, e -> e.java to i }.toMap()
            return this
        }

        fun allowOnlyUniqueItems(): Builder {
            adapter.isAllowedOnlyUniqueItems = true
            return this
        }

        fun build() = adapter
    }
}