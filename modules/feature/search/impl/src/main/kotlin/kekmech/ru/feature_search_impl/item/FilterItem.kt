package kekmech.ru.feature_search_impl.item

import androidx.annotation.StringRes
import kekmech.ru.res_strings.R.string as Strings

internal data class FilterItem(
    val type: FilterItemType = FilterItemType.ALL,
    val isSelected: Boolean = false,
) {
    companion object {
        fun resolveAllItems() = listOf(
            FilterItem(FilterItemType.ALL, true),
            FilterItem(FilterItemType.GROUPS, false),
            FilterItem(FilterItemType.PERSONS, false),
            FilterItem(FilterItemType.NOTES, false),
            FilterItem(FilterItemType.MAP, false)
        )
    }
}

internal enum class FilterItemType(
    @StringRes val nameRes: Int,
) {
    ALL(Strings.search_filter_all),
    GROUPS(Strings.search_filter_groups),
    PERSONS(Strings.search_filter_persons),
    NOTES(Strings.search_filter_notes),
    MAP(Strings.search_filter_map)
}

internal fun FilterItemType.compareFilter(filterItemType: FilterItemType) =
    this == FilterItemType.ALL || this == filterItemType
