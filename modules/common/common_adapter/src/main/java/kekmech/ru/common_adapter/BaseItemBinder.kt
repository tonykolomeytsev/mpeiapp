package kekmech.ru.common_adapter

import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseItemBinder<in VH, in Model> {

    fun bindView(viewHolder: RecyclerView.ViewHolder, model: Any, position: Int) =
        bind(viewHolder as VH, model as Model, position)

    abstract fun bind(vh: VH, model: Model, position: Int)

    fun detachViewHolder(vh: RecyclerView.ViewHolder) =
        unbind(vh as VH)

    open fun unbind(vh: VH) = Unit

    fun updateViewHolder(vh: RecyclerView.ViewHolder, model: Any, position: Int, payloads: List<Any>) =
        update(vh as VH, model as Model, position, payloads)

    open fun update(vh: VH, model: Model, position: Int, payloads: List<Any>) =
        bind(vh, model, position)
}