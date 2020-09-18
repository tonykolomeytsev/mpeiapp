package kekmech.ru.coreui.deprecated.adapter2

import androidx.recyclerview.widget.DiffUtil

class StandartDiffUtilProcessor : BaseAdapter2.DiffUtilProcessor {

    override fun invoke(
        oldItems: List<BaseItem2<*>>,
        newItems: List<BaseItem2<*>>,
        adapter: BaseAdapter2
    ) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldItems.size
            override fun getNewListSize() = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldItems[oldItemPosition].javaClass == newItems[newItemPosition].javaClass
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldItems[oldItemPosition] == newItems[newItemPosition]
        }
        val result = DiffUtil.calculateDiff(diffCallback)
        adapter.items.clear()
        adapter.items.addAll(newItems)
        try {
            result.dispatchUpdatesTo(adapter)
        } catch (e: Exception) {
            adapter.notifyDataSetChanged()
        }
    }
}