package kekmech.ru.bars.details.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface BarsDetailsFragmentView : LifecycleOwner {
    var onNavBackListener: () -> Unit

    fun setEventsAdapter(adapter: RecyclerView.Adapter<*>)
    fun setWeeksAdapter(adapter: RecyclerView.Adapter<*>)
    fun setFinalAdapter(adapter: RecyclerView.Adapter<*>)
    fun setTitle(string: String)
}