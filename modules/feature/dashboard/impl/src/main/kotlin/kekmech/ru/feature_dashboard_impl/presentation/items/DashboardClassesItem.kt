package kekmech.ru.feature_dashboard_impl.presentation.items

import android.content.Context
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_schedule.items.ClassesItemBinder
import kekmech.ru.lib_schedule.items.ClassesViewHolder
import kekmech.ru.lib_schedule.items.ClassesViewHolderImpl
import kekmech.ru.lib_schedule.R as common_schedule_R

internal class DashboardClassesAdapterItem(
    context: Context,
    onClickListener: ((Classes) -> Unit)? = null,
) : AdapterItem<ClassesViewHolder, Classes>(
    isType = { it is Classes },
    layoutRes = common_schedule_R.layout.item_classes_padding_horisontal_16dp,
    viewHolderGenerator = ::ClassesViewHolderImpl,
    itemBinder = ClassesItemBinder(context, onClickListener),
    areItemsTheSame = { a, b -> a.name == b.name }
)
