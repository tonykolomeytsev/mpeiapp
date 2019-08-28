package kekmech.ru.feed

import android.content.Context
import android.support.v7.app.AlertDialog
import kekmech.ru.coreui.adapter.BaseAdapter

interface IFeedFragment {

    var onEditListener: () -> Unit

    var bottomReachListener: () -> Unit

    val activityContext: Context?

    fun setStatus(title: String, subtitle: String)

    fun showEditDialog(dialog: AlertDialog)

    fun updateAdapterIfNull(adapter: BaseAdapter)

    fun unlock()
}