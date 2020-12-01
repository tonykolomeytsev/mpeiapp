package kekmech.ru.feature_dashboard.items

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.viewbinding.ReusableViewHolder
import kekmech.ru.common_android.viewbinding.lazyBinding
import kekmech.ru.coreui.items.SectionHeaderViewHolder
import kekmech.ru.coreui.items.SectionHeaderViewHolderImpl
import kekmech.ru.feature_dashboard.R

data class EventsHeaderItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val subtitle: String? = null,
    val groupName: String
)

interface EventsHeaderViewHolder : SectionHeaderViewHolder {
    fun setGroupName(groupName: String)
    fun setOnGroupClick(onGroupNameCLickListener: () -> Unit)
}

class EventsHeaderViewHolderImpl(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    EventsHeaderViewHolder,
    SectionHeaderViewHolder by SectionHeaderViewHolderImpl(containerView),
    ReusableViewHolder {

    private val textViewGroupName by lazyBinding<TextView>(R.id.textViewGroupName)
    private val linearLayoutAction by lazyBinding<LinearLayout>(R.id.linearLayoutAction)

    override fun setGroupName(groupName: String) {
        textViewGroupName.text = groupName
    }

    override fun setOnGroupClick(onGroupNameCLickListener: () -> Unit) {
        linearLayoutAction.setOnClickListener { onGroupNameCLickListener() }
    }
}

class EventsHeaderItemBinder(
    private val onGroupNameCLickListener: (() -> Unit)?
) : BaseItemBinder<EventsHeaderViewHolder, EventsHeaderItem>() {

    override fun bind(vh: EventsHeaderViewHolder, model: EventsHeaderItem, position: Int) {
        vh.setSubtitleVisibility(model.subtitle != null)
        model.subtitle?.let(vh::setSubtitle)
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
        vh.setGroupName(model.groupName)
        vh.setOnGroupClick { onGroupNameCLickListener?.invoke() }
    }
}

class EventsHeaderAdapterItem(
    onGroupNameCLickListener: (() -> Unit)? = null
) : AdapterItem<EventsHeaderViewHolder, EventsHeaderItem>(
    isType = { it is EventsHeaderItem },
    layoutRes = R.layout.item_events_header,
    viewHolderGenerator = ::EventsHeaderViewHolderImpl,
    itemBinder = EventsHeaderItemBinder(onGroupNameCLickListener),
    areItemsTheSame = { a, b -> a.title == b.title && a.titleRes == b.titleRes }
)