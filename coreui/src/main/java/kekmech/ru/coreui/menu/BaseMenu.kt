package kekmech.ru.coreui.menu


import android.content.Context
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.items.DividerItem
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.coreui.items.TwoLineIconedItem
import java.util.*

class BaseMenu(private var context: Context?) {
    val linkedList = LinkedList<BaseItem<*>>()

    fun divider(header: String, isLineVisible: Boolean = true): BaseMenu {
        linkedList.add(DividerItem(header, isLineVisible))
        return this
    }

    fun divider(stringId: Int, isLineVisible: Boolean = true) =
        divider(context!!.resources.getString(stringId), isLineVisible)

    /**
     * Two-line item with icon
     */
    fun item(header: String, description: String, icon: Int, itemId: String): BaseMenu {
        linkedList.add(TwoLineIconedItem(header, description, icon, itemId))
        return this
    }

    /**
     * Two-line item with icon
     */
    fun item(header: Int, description: Int, icon: Int, itemId: String) =
        item(context!!.resources.getString(header), context!!.resources.getString(description), icon, itemId)

    /**
     * Single-line item without icon
     */
    fun item(header: String, itemId: String): BaseMenu {
        linkedList.add(SingleLineItem(header, itemId))
        return this
    }

    fun buildAdapter(settingsItemListener: ItemListener) = BaseAdapter.Builder()
        .registerViewTypeFactory(TwoLineIconedItem.Factory())
        .registerViewTypeFactory(SingleLineItem.Factory())
        .registerViewTypeFactory(DividerItem.Factory())
        .build()
        .apply {
            context = null
            linkedList.forEach { item ->
                if (item is BaseClickableItem<*>)
                    item.clickListener = { itemId -> settingsItemListener.onItemClick(itemId) }
            }
            baseItems.addAll(linkedList)
        }
}