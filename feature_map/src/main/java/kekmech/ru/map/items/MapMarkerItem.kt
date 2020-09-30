package kekmech.ru.map.items

import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.LabeledTextViewHolder
import kekmech.ru.coreui.items.LabeledTextViewHolderImpl
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.map.R

class MapMarkerItemBinder(
    private val onClickListener: ((MapMarker) -> Unit)?
) : BaseItemBinder<LabeledTextViewHolder, MapMarker>() {

    override fun bind(vh: LabeledTextViewHolder, model: MapMarker, position: Int) {
        model.name.let(vh::setMainText)
        model.address.let(vh::setLabel)
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}

class MapMarkerAdapterItem(
    onClickListener: ((MapMarker) -> Unit)? = null
) : AdapterItem<LabeledTextViewHolder, MapMarker>(
    isType = { it is MapMarker },
    layoutRes = R.layout.item_text_bottom_labeled,
    viewHolderGenerator = ::LabeledTextViewHolderImpl,
    itemBinder = MapMarkerItemBinder(onClickListener)
)