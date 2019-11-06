package kekmech.ru.bars.details.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface BarsDetailsFragmentView : LifecycleOwner {
    fun setEventsAdapter(adapter: RecyclerView.Adapter<*>)
    fun setWeeksAdapter(adapter: RecyclerView.Adapter<*>)
    fun setFinalAdapter(adapter: RecyclerView.Adapter<*>)
    fun setTitle(string: String)
}