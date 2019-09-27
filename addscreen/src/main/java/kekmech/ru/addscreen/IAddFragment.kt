package kekmech.ru.addscreen

import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner

interface IAddFragment : LifecycleOwner {
    val web: WebView

    var onSearchClickListener: (String) -> Unit

    fun hideLoadButton()
    fun showLoadButton()
    fun disableEditText()
    fun enableEditText()
    fun showLoading()
    fun hideLoading()
}