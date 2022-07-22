package kekmech.ru.feature_search.item

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_search.R
import kekmech.ru.strings.Strings

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

internal class FilterAdapterItem(
    onCLickListener: ((FilterItem) -> Unit)? = null,
) : AdapterItem<FilterViewHolder, FilterItem>(
    isType = { it is FilterItem },
    layoutRes = R.layout.item_filter,
    viewHolderGenerator = ::FilterViewHolder,
    itemBinder = FilterItemBinder(onCLickListener),
    areItemsTheSame = { a, b -> a.type == b.type }
)

internal fun FilterItemType.compareFilter(filterItemType: FilterItemType) =
    this == FilterItemType.ALL || this == filterItemType

internal class FilterViewHolder(
    private val containerView: View,
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    fun setText(@StringRes textRes: Int) {
        (containerView as TextView).setText(textRes)
    }

    fun setIsSelected(isSelected: Boolean) {
        (containerView as TextView).isSelected = isSelected
    }
}

private class FilterItemBinder(
    private val onCLickListener: ((FilterItem) -> Unit)?,
) : BaseItemBinder<FilterViewHolder, FilterItem>() {
    override fun bind(vh: FilterViewHolder, model: FilterItem, position: Int) {
        vh.setText(model.type.nameRes)
        vh.setIsSelected(model.isSelected)
        vh.setOnClickListener { onCLickListener?.invoke(model) }
    }
}
