package kekmech.ru.lib_schedule.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.lib_schedule.R

object SelfStudyItem

interface SelfStudyViewHolder

private class SelfStudyViewHolderImpl(
    containerView: View
) : SelfStudyViewHolder, RecyclerView.ViewHolder(containerView)

private class SelfStudyItemBinder : BaseItemBinder<SelfStudyViewHolder, SelfStudyItem>() {
    override fun bind(vh: SelfStudyViewHolder, model: SelfStudyItem, position: Int) = Unit
}

class SelfStudyAdapterItem : AdapterItem<SelfStudyViewHolder, SelfStudyItem>(
    isType = { it is SelfStudyItem },
    layoutRes = R.layout.item_self_study,
    viewHolderGenerator = ::SelfStudyViewHolderImpl,
    itemBinder = SelfStudyItemBinder()
)
