package kekmech.ru.bars.main.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.dto.AcademicScore

interface BarsFragmentView : LifecycleOwner {
    var onRefreshListener: () -> Unit

    fun hideLoading()

    fun showLoading()

    fun setLoginState(boolean: Boolean)
}