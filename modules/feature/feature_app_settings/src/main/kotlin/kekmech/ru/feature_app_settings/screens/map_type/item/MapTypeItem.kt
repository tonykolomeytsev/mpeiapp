package kekmech.ru.feature_app_settings.screens.map_type.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.ItemMapTypeBinding
import kekmech.ru.feature_app_settings.screens.map_type.dto.MapTypeEntry

internal data class MapTypeItem(
    val type: MapTypeEntry,
    val isSelected: Boolean
)

internal class MapTypeAdapterItem(
    onClickListener: (MapTypeEntry) -> Unit
) : AdapterItem<MapTypeViewHolder, MapTypeItem>(
    isType = { it is MapTypeItem },
    layoutRes = R.layout.item_map_type,
    viewHolderGenerator = ::MapTypeViewHolder,
    itemBinder = LanguageItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.type == b.type }
)

internal class MapTypeViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemMapTypeBinding.bind(containerView)

    fun setDescription(descriptionRes: Int) {
        viewBinding.textViewDescription.setText(descriptionRes)
    }

    fun setIsSelected(isSelected: Boolean) {
        viewBinding.indicatorContainer.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

private class LanguageItemBinder(
    private val onClickListener: (MapTypeEntry) -> Unit
) : BaseItemBinder<MapTypeViewHolder, MapTypeItem>() {

    override fun bind(vh: MapTypeViewHolder, model: MapTypeItem, position: Int) {
        vh.setDescription(model.type.descriptionRes)
        vh.setIsSelected(model.isSelected)
        vh.setOnClickListener { onClickListener(model.type) }
    }
}
