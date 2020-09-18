package kekmech.ru.coreui.deprecated.adapter

import java.util.*

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of Mr. Captain project.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseItem<T : BaseViewHolder> {

    var cacheType: Int = -1

    open fun updateViewHolderNative(viewHolder: BaseViewHolder) {
        updateViewHolder(viewHolder as T)
    }

    abstract fun updateViewHolder(viewHolder: T)

    fun getType(viewFactories: HashMap<Int, BaseFactory>): Int {
        if (cacheType == -1) viewFactories.any { approveNative(it.key, it.value) }
        return cacheType
    }

    private fun approveNative(type: Int, factory: BaseFactory): Boolean {
        if (approveFactory(factory)) {
            cacheType = type
            return true
        }
        return false
    }

    abstract fun approveFactory(factory: BaseFactory): Boolean
}