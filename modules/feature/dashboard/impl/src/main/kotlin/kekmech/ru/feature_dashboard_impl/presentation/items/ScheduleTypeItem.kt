package kekmech.ru.feature_dashboard_impl.presentation.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.feature_dashboard_impl.databinding.ItemScheduleTypeBinding
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.res_icons.R.drawable as Icons

internal data class ScheduleTypeItem(
    val selectedScheduleName: String,
    val selectedScheduleType: ScheduleType,
)

interface ScheduleTypeViewHolder : ClickableItemViewHolder {

    fun setScheduleName(name: String)
    fun setScheduleTypeIcon(@DrawableRes iconRes: Int)
}

private class ScheduleTypeViewHolderImpl(
    containerView: View,
) :
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    ScheduleTypeViewHolder {

    private val viewBinding = ItemScheduleTypeBinding.bind(containerView)

    override fun setScheduleName(name: String) {
        viewBinding.scheduleName.text = name
    }

    override fun setScheduleTypeIcon(iconRes: Int) {
        viewBinding.imageView.setImageResource(iconRes)
    }
}

private class ScheduleTypeItemBinder(
    private val onClickListener: () -> Unit,
) : BaseItemBinder<ScheduleTypeViewHolder, ScheduleTypeItem>() {

    override fun bind(vh: ScheduleTypeViewHolder, model: ScheduleTypeItem, position: Int) {
        vh.setScheduleName(model.selectedScheduleName)
        vh.setScheduleTypeIcon(
            when (model.selectedScheduleType) {
                ScheduleType.GROUP -> Icons.ic_groups_black_24
                ScheduleType.PERSON -> R.drawable.ic_person_24
            }
        )
        vh.setOnClickListener { onClickListener() }
    }
}

internal class ScheduleTypeAdapterItem(
    onClickListener: () -> Unit,
) : AdapterItem<ScheduleTypeViewHolder, ScheduleTypeItem>(
    isType = { it is ScheduleTypeItem },
    layoutRes = R.layout.item_schedule_type,
    viewHolderGenerator = ::ScheduleTypeViewHolderImpl,
    itemBinder = ScheduleTypeItemBinder(onClickListener)
)
