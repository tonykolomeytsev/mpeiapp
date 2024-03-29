package kekmech.ru.feature_dashboard_impl.presentation.items

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_dashboard_impl.R

internal object BannerOpenSourceItem

internal class BannerOpenSourceAdapterItem(
    onClickListener: ((BannerOpenSourceItem) -> Unit)? = null
) : ClickableAdapterItem<BannerOpenSourceItem>(
    isType = { it is BannerOpenSourceItem },
    layoutRes = R.layout.item_banner_open_source,
    onClickListener = onClickListener
)
