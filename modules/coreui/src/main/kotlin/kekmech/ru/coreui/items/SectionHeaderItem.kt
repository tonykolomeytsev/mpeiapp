package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

private const val ITEM_SECTION_HEADER_DEFAULT_ID = 0

data class SectionHeaderItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val subtitle: String? = null,
    @StringRes val subtitleRes: Int? = null,
    val itemId: Int = ITEM_SECTION_HEADER_DEFAULT_ID,
    @StringRes val actionNameRes: Int? = null
)

interface SectionHeaderViewHolder {
    fun setTitle(title: String)
    fun setTitle(@StringRes resId: Int)
    fun setSubtitle(subtitle: String)
    fun setSubtitle(@StringRes resId: Int)
    fun setSubtitleVisibility(isVisible: Boolean)
    fun setActionName(actionNameRes: Int)
    fun setOnActionClickListener(listener: (View) -> Unit)
}

class SectionHeaderViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), SectionHeaderViewHolder {

    private val textViewTitle = containerView.findViewById<TextView>(R.id.textViewTitle)
    private val textViewSubtitle = containerView.findViewById<TextView>(R.id.textViewSubtitle)
    private val textViewAction by fastLazy { containerView.findViewById<TextView>(R.id.textViewAction) }

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setTitle(@StringRes resId: Int) {
        textViewTitle.setText(resId)
    }

    override fun setSubtitle(subtitle: String) {
        textViewSubtitle.text = subtitle
    }

    override fun setSubtitle(resId: Int) {
        textViewSubtitle.setText(resId)
    }

    override fun setSubtitleVisibility(isVisible: Boolean) {
        textViewSubtitle.isVisible = isVisible
    }

    override fun setActionName(actionNameRes: Int) {
        if (actionNameRes != 0) {
            textViewAction.isVisible = true
            textViewAction.setText(actionNameRes)
        } else {
            textViewAction.isVisible = false
        }
    }

    override fun setOnActionClickListener(listener: (View) -> Unit) {
        textViewAction.setOnClickListener(listener)
    }
}

class SectionHeaderItemBinder(
    private val onActionClickListener: ((SectionHeaderItem) -> Unit)?
) : BaseItemBinder<SectionHeaderViewHolder, SectionHeaderItem>() {

    override fun bind(vh: SectionHeaderViewHolder, model: SectionHeaderItem, position: Int) {
        vh.setSubtitleVisibility(model.subtitle != null)
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
        model.subtitle?.let(vh::setSubtitle)
        model.subtitleRes?.let(vh::setSubtitle)
        vh.setActionName(model.actionNameRes ?: 0)
        vh.setOnActionClickListener { onActionClickListener?.invoke(model) }
    }
}

class SectionHeaderAdapterItem(
    itemId: Int = ITEM_SECTION_HEADER_DEFAULT_ID,
    onActionClickListener: ((SectionHeaderItem) -> Unit)? = null
) : AdapterItem<SectionHeaderViewHolder, SectionHeaderItem>(
    isType = { it is SectionHeaderItem && it.itemId == itemId },
    layoutRes = R.layout.item_section_header,
    viewHolderGenerator = ::SectionHeaderViewHolderImpl,
    itemBinder = SectionHeaderItemBinder(onActionClickListener),
    areItemsTheSame = { a, b -> a.title == b.title && a.titleRes == b.titleRes }
)
