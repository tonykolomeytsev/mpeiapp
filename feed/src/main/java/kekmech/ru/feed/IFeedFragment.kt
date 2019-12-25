package kekmech.ru.feed

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import kekmech.ru.coreui.adapter.BaseAdapter

interface IFeedFragment : LifecycleOwner {

    var onSettingsClick: () -> Unit

    fun setAdapter(adapter: BaseAdapter)

    fun withinContext(listener: (context: Context) -> Unit)

    fun showLoading()

    fun hideLoading()
}