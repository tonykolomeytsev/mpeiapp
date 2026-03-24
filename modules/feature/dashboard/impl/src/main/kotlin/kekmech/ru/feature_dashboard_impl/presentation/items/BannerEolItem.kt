package kekmech.ru.feature_dashboard_impl.presentation.items

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_dashboard_impl.R

internal object BannerEolItem

internal class BannerEolAdapterItem(
    onClickListener: (BannerEolItem) -> Unit
) : ClickableAdapterItem<BannerEolItem>(
    isType = { it is BannerEolItem },
    layoutRes = R.layout.item_banner_eol,
    onClickListener = onClickListener
)
