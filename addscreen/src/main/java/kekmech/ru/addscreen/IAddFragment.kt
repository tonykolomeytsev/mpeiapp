package kekmech.ru.addscreen

import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner
import kekmech.ru.coreui.adapter.BaseAdapter

interface IAddFragment : LifecycleOwner {

    var onSearchClickListener: (String) -> Unit

    fun hideLoadButton()
    fun showLoadButton()
    fun disableEditText()
    fun enableEditText()
    fun showLoading()
    fun hideLoading()

    fun setAdapter(adapter: BaseAdapter)
    fun showError()
}