package kekmech.ru.common_adapter

import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
public abstract class BaseItemBinder<in VH, in Model> {

    public fun bindView(viewHolder: RecyclerView.ViewHolder, model: Any, position: Int) {
        bind(viewHolder as VH, model as Model, position)
    }

    public abstract fun bind(vh: VH, model: Model, position: Int)

    public fun detachViewHolder(vh: RecyclerView.ViewHolder) {
        unbind(vh as VH)
    }

    public open fun unbind(vh: VH): Unit = Unit

    public fun updateViewHolder(vh: RecyclerView.ViewHolder, model: Any, position: Int, payloads: List<Any>) {
        update(vh as VH, model as Model, position, payloads)
    }

    public open fun update(vh: VH, model: Model, position: Int, payloads: List<Any>) {
        bind(vh, model, position)
    }
}
