package kekmech.ru.bars.main.adapter

import android.view.View
import kekmech.ru.bars.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder2

class ProfilePieceItem(val header: String, val description: String) : BaseItem<ProfilePieceItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.header = header
        viewHolder.description = description
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        var header by bindText(R.id.textViewProfilePieceHeader)
        var description by bindText(R.id.textViewProfilePieceDescription)
    }

    class Factory : BaseFactory(R.layout.item_profile_piece, ::ViewHolder)
}