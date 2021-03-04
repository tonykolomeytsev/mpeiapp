package kekmech.ru.feature_dashboard.items

import android.content.Context
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_schedule.items.ClassesItemBinder
import kekmech.ru.common_schedule.items.ClassesViewHolder
import kekmech.ru.common_schedule.items.ClassesViewHolderImpl
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_dashboard.R

class DashboardClassesAdapterItem(
    context: Context,
    onClickListener: ((Classes) -> Unit)? = null
) : AdapterItem<ClassesViewHolder, Classes>(
    isType = { it is Classes },
    layoutRes = R.layout.item_classes_padding_horisontal_16dp,
    viewHolderGenerator = ::ClassesViewHolderImpl,
    itemBinder = ClassesItemBinder(context, onClickListener)
)