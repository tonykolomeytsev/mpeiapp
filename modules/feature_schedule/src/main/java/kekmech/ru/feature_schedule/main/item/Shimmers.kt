package kekmech.ru.feature_schedule.main.item

import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.feature_schedule.R

private const val CLASSES_SHIMMER_ITEM = 3

fun getClassesShimmerItem() = ShimmerItem(CLASSES_SHIMMER_ITEM)

class ClassesShimmerAdapterItem : ShimmerAdapterItem(CLASSES_SHIMMER_ITEM, R.layout.item_working_day_shimmer)
