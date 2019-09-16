package kekmech.ru.feed

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kekmech.ru.coreui.adapter.BaseAdapter

interface IFeedFragment {

    var onEditListener: () -> Unit

    var bottomReachListener: () -> Unit

    fun withinContext(listener: (context: Context) -> Unit)

    fun setStatus(title: String, dayInfo: String, weekInfo: String)

    fun showEditDialog(dialog: AlertDialog)

    fun updateAdapterIfNull(adapter: BaseAdapter)

    fun unlock()
}