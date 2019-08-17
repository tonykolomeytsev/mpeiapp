package kekmech.ru.settings


import android.content.Context
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import java.util.*

class Menu(private var context: Context?) {
    val linkedList = LinkedList<BaseItem<*>>()

    fun divider(header: String, isLineVisible: Boolean = true): Menu {
        linkedList.add(SettingsDividerItem(header, isLineVisible))
        return this
    }

    fun divider(stringId: Int, isLineVisible: Boolean = true) =
        divider(context!!.resources.getString(stringId), isLineVisible)

    fun item(header: String, description: String, icon: Int): Menu {
        linkedList.add(SettingsItem(header, description, icon))
        return this
    }
    
    fun item(header: Int, description: Int, icon: Int) =
        item(context!!.resources.getString(header), context!!.resources.getString(description), icon)

    fun buildAdapter() = BaseAdapter.Builder()
        .registerViewTypeFactory(SettingsItem.Factory())
        .registerViewTypeFactory(SettingsDividerItem.Factory())
        .build()
        .apply {
            context = null
            baseItems.addAll(linkedList)
        }
}