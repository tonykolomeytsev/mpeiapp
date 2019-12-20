package kekmech.ru.feed

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import kekmech.ru.coreui.adapter.BaseAdapter

interface IFeedFragment : LifecycleOwner {

    fun setAdapter(adapter: BaseAdapter)

    fun withinContext(listener: (context: Context) -> Unit)
}