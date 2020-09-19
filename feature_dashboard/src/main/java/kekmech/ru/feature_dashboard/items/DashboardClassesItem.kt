package kekmech.ru.feature_dashboard.items

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.coreui.items.ClassesItemBinder
import kekmech.ru.coreui.items.ClassesViewHolder
import kekmech.ru.coreui.items.ClassesViewHolderImpl
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_dashboard.R
import kotlinx.android.extensions.LayoutContainer

interface DashboardClassesMinViewHolder : ClassesViewHolder

class DashboardClassesMinViewHolderImpl(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    ClassesViewHolder by ClassesViewHolderImpl(containerView),
    LayoutContainer,
    DashboardClassesMinViewHolder
{
    override fun setNumberName(name: String) = Unit

    override fun setPersonName(name: String?) = Unit

    override fun setTagColor(color: Int) = Unit

    override fun setTypeName(name: String) = Unit
}

class DashboardClassesAdapterItem(context: Context) : AdapterItem<ClassesViewHolder, Classes>(
    isType = { it is Classes && it.stackType == null },
    layoutRes = R.layout.item_dashboard_classes,
    viewHolderGenerator = ::ClassesViewHolderImpl,
    itemBinder = ClassesItemBinder(context)
)

class DashboardClassesMinAdapterItem(context: Context) : AdapterItem<ClassesViewHolder, Classes>(
    isType = { it is Classes && it.stackType != null },
    layoutRes = R.layout.item_dashboard_classes_min,
    viewHolderGenerator = ::DashboardClassesMinViewHolderImpl,
    itemBinder = ClassesItemBinder(context)
)