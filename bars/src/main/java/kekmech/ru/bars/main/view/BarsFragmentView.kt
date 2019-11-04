package kekmech.ru.bars.main.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentView : LifecycleOwner {

    var state: State

    var onLogInListener: (String, String) -> Unit

    var onRightsClickListener: () -> Unit

    var onRefreshListener: () -> Unit

    var onLogoutListener: () -> Unit

    fun setAdapter(adapter: RecyclerView.Adapter<*>)

    fun setStatus(score: AcademicScore)

    fun hideLoading()

    fun showLoading()

    enum class State { LOGIN, SCORE }
}