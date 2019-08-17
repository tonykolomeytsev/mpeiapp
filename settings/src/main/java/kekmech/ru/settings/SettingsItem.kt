package kekmech.ru.settings

import android.graphics.drawable.Drawable
import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder

class SettingsItem(val header: String, val description: String, val icon: Int) : BaseItem<SettingsItem.ViewHolder>() {

    override fun updateViewHolder(vh: ViewHolder) {
        vh.description.text = description
        vh.header.text = header
        vh.icon.setImageResource(icon)
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val header by bind<TextView>(R.id.textViewPrimary)
        val description by bind<TextView>(R.id.textViewSecondary)
        val icon by bind<ImageView>(R.id.imageViewAvatar)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_settings_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }
}