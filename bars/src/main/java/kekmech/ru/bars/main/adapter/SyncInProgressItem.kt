package kekmech.ru.bars.main.adapter

import android.view.View
import kekmech.ru.bars.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder2

class SyncInProgressItem : BaseItem<SyncInProgressItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) = Unit

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder2(view)

    class Factory : BaseFactory(R.layout.item_sync_in_progress, ::ViewHolder)
}