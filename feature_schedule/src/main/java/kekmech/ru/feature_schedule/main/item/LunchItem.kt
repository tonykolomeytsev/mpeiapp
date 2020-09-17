package kekmech.ru.feature_schedule.main.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_schedule.R
import kotlinx.android.extensions.LayoutContainer

object LunchItem

interface LunchViewHolder

class LunchViewHolderImpl(
    override val containerView: View
) : LunchViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer

class LunchItemBinder : BaseItemBinder<SelfStudyViewHolder, LunchItem>() {

    override fun bind(vh: SelfStudyViewHolder, model: LunchItem, position: Int) = Unit
}

class LunchAdapterItem : AdapterItem<SelfStudyViewHolder, LunchItem>(
    isType = { it is LunchItem },
    layoutRes = R.layout.item_lunch,
    viewHolderGenerator = ::SelfStudyViewHolderImpl,
    itemBinder = LunchItemBinder()
)