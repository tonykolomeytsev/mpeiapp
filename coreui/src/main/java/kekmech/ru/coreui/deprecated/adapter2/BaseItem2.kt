package kekmech.ru.coreui.deprecated.adapter2

import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class BaseItem2<T : RecyclerView.ViewHolder>(
    val layoutId: Int,
    val viewHolderClass: KClass<T>
) {
    abstract fun updateViewHolder(vh: T)

    fun updateViewHolderNative(holder: RecyclerView.ViewHolder) {
        updateViewHolder(holder as T)
    }
}