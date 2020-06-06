package kekmech.ru.map.view.pages

import android.view.View
import android.widget.TextView
import com.example.map.R
import kekmech.ru.core.dto.Building
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder

class SingleBuildingItem(val building: Building) : BaseClickableItem<SingleBuildingItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = building.name
        viewHolder.address.text = building.address
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewPlaceName)
        val address by bind<TextView>(R.id.textViewPlaceAddress)
    }

    class Factory : BaseFactory(R.layout.item_single_building,
        SingleBuildingItem::ViewHolder
    )
}