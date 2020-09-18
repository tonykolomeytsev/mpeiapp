package kekmech.ru.map.view.pages

import android.view.View
import android.widget.TextView
import com.example.map.R
import kekmech.ru.core.dto.Hostel
import kekmech.ru.coreui.deprecated.adapter.BaseClickableItem
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder

class SingleHostelItem(val hostel: Hostel) : BaseClickableItem<SingleHostelItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = hostel.name
        viewHolder.address.text = hostel.address
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewPlaceName)
        val address by bind<TextView>(R.id.textViewPlaceAddress)
    }

    class Factory : BaseFactory(R.layout.item_single_hostel,
        SingleHostelItem::ViewHolder
    )
}