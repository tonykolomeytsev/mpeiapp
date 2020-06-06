package kekmech.ru.addscreen.presenter

import android.view.View
import kekmech.ru.addscreen.R
import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.coreui.adapter.BaseViewHolder2
import kekmech.ru.coreui.adapter2.BaseItem2

class GroupItem(val group: AcademGroup) : BaseItem2<GroupItem.ViewHolder>(R.layout.item_group, ViewHolder::class) {

    var clickListener: (GroupItem) -> Unit = {}

    override fun updateViewHolder(vh: ViewHolder) {
        vh.name = group.name
        vh.itemView.setOnClickListener { clickListener(this) }
    }

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        var name by bindText(R.id.textViewGroupName)
    }
}