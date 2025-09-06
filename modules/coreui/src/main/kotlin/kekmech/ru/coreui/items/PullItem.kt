package kekmech.ru.coreui.items

import kekmech.ru.coreui.R

public object PullItem

public class PullAdapterItem : ClickableAdapterItem<PullItem>(
    isType = { it is PullItem },
    layoutRes = R.layout.item_pull
)
