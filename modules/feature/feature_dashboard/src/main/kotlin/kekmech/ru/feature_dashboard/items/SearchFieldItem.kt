package kekmech.ru.feature_dashboard.items

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_dashboard.R

object SearchFieldItem

class SearchFieldAdapterItem(
    onClickListener: (SearchFieldItem) -> Unit
) : ClickableAdapterItem<SearchFieldItem>(
    isType = { it is SearchFieldItem },
    layoutRes = R.layout.item_search_field,
    onClickListener = onClickListener
)
