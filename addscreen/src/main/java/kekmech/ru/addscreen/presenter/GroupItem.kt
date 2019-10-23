package kekmech.ru.addscreen.presenter

import android.view.View
import android.widget.TextView
import kekmech.ru.addscreen.R
import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder

class GroupItem(val group: AcademGroup) : BaseClickableItem<GroupItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = group.name
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewGroupName)
    }

    class Factory : BaseFactory(R.layout.item_group) {
        override fun instance(view: View) = ViewHolder(view)
    }
}