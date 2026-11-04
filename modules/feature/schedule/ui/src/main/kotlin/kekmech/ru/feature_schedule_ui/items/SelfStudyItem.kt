package kekmech.ru.feature_schedule_ui.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule_ui.R
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

public object SelfStudyItem

public interface SelfStudyViewHolder

private class SelfStudyViewHolderImpl(
    containerView: View
) : SelfStudyViewHolder, RecyclerView.ViewHolder(containerView)

private class SelfStudyItemBinder : BaseItemBinder<SelfStudyViewHolder, SelfStudyItem>() {
    override fun bind(vh: SelfStudyViewHolder, model: SelfStudyItem, position: Int) = Unit
}

public class SelfStudyAdapterItem : AdapterItem<SelfStudyViewHolder, SelfStudyItem>(
    isType = { it is SelfStudyItem },
    layoutRes = R.layout.item_self_study,
    viewHolderGenerator = ::SelfStudyViewHolderImpl,
    itemBinder = SelfStudyItemBinder()
)
