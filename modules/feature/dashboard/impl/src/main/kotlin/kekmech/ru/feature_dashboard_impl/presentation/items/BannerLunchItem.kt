package kekmech.ru.feature_dashboard_impl.presentation.items

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_dashboard_impl.R

internal object BannerLunchItem

internal class BannerLunchAdapterItem(
    onClickListener: (BannerLunchItem) -> Unit
) : ClickableAdapterItem<BannerLunchItem>(
    isType = { it is BannerLunchItem },
    layoutRes = R.layout.item_banner_lunch,
    onClickListener = onClickListener
)
