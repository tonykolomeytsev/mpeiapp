package kekmech.ru.feature_dashboard.items

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_dashboard.R

internal object SearchFieldItem

internal class SearchFieldAdapterItem(
    onClickListener: (SearchFieldItem) -> Unit
) : ClickableAdapterItem<SearchFieldItem>(
    isType = { it is SearchFieldItem },
    layoutRes = R.layout.item_search_field,
    onClickListener = onClickListener
)
