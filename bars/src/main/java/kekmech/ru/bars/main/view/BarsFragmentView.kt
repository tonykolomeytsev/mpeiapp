package kekmech.ru.bars.main.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
interface BarsFragmentView : LifecycleOwner {

    var state: State

    var onLogInListener: (String, String) -> Unit

    fun setAdapter(adapter: RecyclerView.Adapter<*>)

    enum class State { LOGIN, SCORE }
}