package kekmech.ru.coreui.adapter

import kotlin.reflect.KClass

open class BaseSortedAdapter : BaseAdapter() {

    private var order: Map<Class<out BaseItem<*>>, Int> = emptyMap()

    override fun addItem(baseItem: BaseItem<*>) {
        fun index(item: BaseItem<*>) = order[item::class.java]
        items += baseItem
        items.sortBy(::index)
        notifyItemInserted(items.indexOf(baseItem))
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

        fun build() = adapter
    }
}