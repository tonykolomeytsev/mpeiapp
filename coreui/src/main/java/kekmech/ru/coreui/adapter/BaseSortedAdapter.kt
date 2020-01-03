package kekmech.ru.coreui.adapter

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