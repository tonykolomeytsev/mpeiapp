package kekmech.ru.feature_schedule.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule.main.item.WeekAdapterItem
import kekmech.ru.feature_schedule.main.item.WeekItem
import kekmech.ru.feature_schedule.main.item.WeekViewHolderImpl

class WeeksScrollAdapter(
    private val weekAdapterItem: WeekAdapterItem
) : RecyclerView.Adapter<WeekViewHolderImpl>() {

    private val allData = HashMap<Int, WeekItem>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.scrollToPosition(Int.MAX_VALUE / 2)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun onBindViewHolder(holder: WeekViewHolderImpl, position: Int) {
        val weekOffset = position - (Int.MAX_VALUE / 2)
        val weekItem = allData[weekOffset] ?: return
        weekAdapterItem.itemBinder.bind(holder, weekItem, weekOffset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolderImpl = LayoutInflater
        .from(parent.context)
        .inflate(weekAdapterItem.layoutRes, parent, false)
        .let(weekAdapterItem.viewHolderGenerator) as WeekViewHolderImpl

    fun update(newData: HashMap<Int, WeekItem>) {
        val changedKeys = newData.keys - allData.keys
        allData.putAll(newData)
        changedKeys.forEach { key ->
            val adapterPosition = key + (Int.MAX_VALUE / 2)
            notifyItemChanged(adapterPosition)
        }
    }
}