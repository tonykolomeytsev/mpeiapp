package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_section_header.*

private const val ITEM_SECTION_HEADER_DEFAULT_ID = 0

data class SectionHeaderItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val subtitle: String? = null,
    val itemId: Int = ITEM_SECTION_HEADER_DEFAULT_ID
)

interface SectionHeaderViewHolder {
    fun setTitle(title: String)
    fun setTitle(@StringRes resId: Int)
    fun setSubtitle(subtitle: String)
    fun setSubtitleVisibility(isVisible: Boolean)
}

class SectionHeaderViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), SectionHeaderViewHolder, LayoutContainer {

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setTitle(@StringRes resId: Int) {
        textViewTitle.setText(resId)
    }

    override fun setSubtitle(subtitle: String) {
        textViewSubtitle.text = subtitle
    }

    override fun setSubtitleVisibility(isVisible: Boolean) {
        textViewSubtitle.isVisible = isVisible
    }
}

class SectionHeaderItemBinder : BaseItemBinder<SectionHeaderViewHolder, SectionHeaderItem>() {

    override fun bind(vh: SectionHeaderViewHolder, model: SectionHeaderItem, position: Int) {
        vh.setSubtitleVisibility(model.subtitle != null)
        model.subtitle?.let(vh::setSubtitle)
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
    }
}

class SectionHeaderAdapterItem(
    itemId: Int = ITEM_SECTION_HEADER_DEFAULT_ID
) : AdapterItem<SectionHeaderViewHolder, SectionHeaderItem>(
    isType = { it is SectionHeaderItem && it.itemId == itemId },
    layoutRes = R.layout.item_section_header,
    viewHolderGenerator = ::SectionHeaderViewHolderImpl,
    itemBinder = SectionHeaderItemBinder(),
    areItemsTheSame = { a, b -> a.title == b.title && a.titleRes == b.titleRes }
)