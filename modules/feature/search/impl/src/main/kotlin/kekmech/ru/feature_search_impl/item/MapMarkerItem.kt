package kekmech.ru.feature_search_impl.item

import kekmech.ru.coreui.items.LabeledTextViewHolder
import kekmech.ru.coreui.items.LabeledTextViewHolderImpl
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.coreui.R as coreui_R

internal class MapMarkerAdapterItem(
    onClickListener: ((MapMarker) -> Unit)? = null,
) : AdapterItem<LabeledTextViewHolder, MapMarker>(
    isType = { it is MapMarker },
    layoutRes = coreui_R.layout.item_text_bottom_labeled,
    viewHolderGenerator = ::LabeledTextViewHolderImpl,
    itemBinder = MapMarkerItemBinder(onClickListener)
)

internal class MapMarkerItemBinder(
    private val onClickListener: ((MapMarker) -> Unit)?,
) : BaseItemBinder<LabeledTextViewHolder, MapMarker>() {

    override fun bind(vh: LabeledTextViewHolder, model: MapMarker, position: Int) {
        model.name.let(vh::setMainText)
        model.address.let(vh::setLabel)
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}
