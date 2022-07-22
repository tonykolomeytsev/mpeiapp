package kekmech.ru.coreui.items

import kekmech.ru.coreui.R

object PullItem

class PullAdapterItem : ClickableAdapterItem<PullItem>(
    isType = { it is PullItem },
    layoutRes = R.layout.item_pull
)
