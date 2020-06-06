package kekmech.ru.addscreen

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface IAddFragment : LifecycleOwner {

    var onSearchClickListener: (String) -> Unit

    fun hideLoadButton()
    fun showLoadButton()
    fun disableEditText()
    fun enableEditText()
    fun showLoading()
    fun hideLoading()

    fun setAdapter(adapter: RecyclerView.Adapter<*>)
    fun showError()
}