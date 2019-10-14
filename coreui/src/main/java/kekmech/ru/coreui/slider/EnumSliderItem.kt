package kekmech.ru.coreui.slider

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class EnumSliderItem(val string: String) : BaseItem<EnumSliderItem.ViewHolder>() {
    var alpha: Float = 1f

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.number.text = string
        viewHolder.number.alpha = 1f; // alpha
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val number by bind<TextView>(R.id.sliderNumber)
    }

    class Factory : BaseFactory(R.layout.item_enum_slider) {
        override fun instance(view: View) = ViewHolder(view)
    }
}