package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer

object SelfStudyItem

interface SelfStudyViewHolder

class SelfStudyViewHolderImpl(
    override val containerView: View
) : SelfStudyViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer

class SelfStudyItemBinder : BaseItemBinder<SelfStudyViewHolder, SelfStudyItem>() {

    override fun bind(vh: SelfStudyViewHolder, model: SelfStudyItem, position: Int) = Unit
}

class SelfStudyAdapterItem : AdapterItem<SelfStudyViewHolder, SelfStudyItem>(
    isType = { it is SelfStudyItem },
    layoutRes = R.layout.item_self_study,
    viewHolderGenerator = ::SelfStudyViewHolderImpl,
    itemBinder = SelfStudyItemBinder()
)