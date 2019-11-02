package kekmech.ru.bars.main.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
interface BarsFragmentView : LifecycleOwner {

    fun setAdapter(adapter: RecyclerView.Adapter<*>)
}