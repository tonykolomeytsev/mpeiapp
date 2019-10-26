package com.example.map.view.pages

import android.view.View
import android.widget.TextView
import com.example.map.R
import kekmech.ru.core.dto.Food
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder

class SingleFoodItem(val food: Food) : BaseClickableItem<SingleFoodItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = food.name
        viewHolder.address.text = food.address
        viewHolder.type.text = food.department
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewPlaceName)
        val address by bind<TextView>(R.id.textViewPlaceAddress)
        val type by bind<TextView>(R.id.textViewPlaceType)
    }

    class Factory : BaseFactory(R.layout.item_single_food,
        SingleFoodItem::ViewHolder
    )
}