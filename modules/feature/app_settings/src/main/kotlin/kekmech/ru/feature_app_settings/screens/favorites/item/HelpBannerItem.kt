package kekmech.ru.feature_app_settings.screens.favorites.item

import kekmech.ru.coreui.items.ClickableAdapterItem
import kekmech.ru.feature_app_settings.R

internal object HelpBannerItem

internal class HelpBannerAdapterItem : ClickableAdapterItem<HelpBannerItem>(
    isType = { it is HelpBannerItem },
    layoutRes = R.layout.item_favorites_help
)
