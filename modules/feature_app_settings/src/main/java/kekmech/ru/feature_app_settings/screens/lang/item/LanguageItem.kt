package kekmech.ru.feature_app_settings.screens.lang.item

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.ItemLanguageBinding
import kekmech.ru.feature_app_settings.screens.lang.dto.LanguageEntry

internal data class LanguageItem(
    val language: LanguageEntry,
    val isSelected: Boolean,
)

internal interface LanguageViewHolder : ClickableItemViewHolder {
    fun setDescription(@StringRes descriptionRes: Int)
    fun setIcon(@DrawableRes iconRes: Int)
    fun setIsSelected(isSelected: Boolean)
}

private class LanguageViewHolderImpl(
    containerView: View,
) : RecyclerView.ViewHolder(containerView),
    LanguageViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemLanguageBinding.bind(containerView)

    override fun setDescription(descriptionRes: Int) {
        viewBinding.textViewDescription.setText(descriptionRes)
    }

    override fun setIcon(iconRes: Int) {
        viewBinding.imageViewIcon.setImageResource(iconRes)
    }

    override fun setIsSelected(isSelected: Boolean) {
        viewBinding.indicatorContainer.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

private class LanguageItemBinder(
    private val onClickListener: (LanguageEntry) -> Unit,
) : BaseItemBinder<LanguageViewHolder, LanguageItem>() {

    override fun bind(vh: LanguageViewHolder, model: LanguageItem, position: Int) {
        vh.setDescription(model.language.descriptionRes)
        vh.setIcon(model.language.iconRes)
        vh.setIsSelected(model.isSelected)
        vh.setOnClickListener { onClickListener(model.language) }
    }
}

internal class LanguageAdapterItem(
    onClickListener: (LanguageEntry) -> Unit,
) : AdapterItem<LanguageViewHolder, LanguageItem>(
    isType = { it is LanguageItem },
    layoutRes = R.layout.item_language,
    viewHolderGenerator = ::LanguageViewHolderImpl,
    itemBinder = LanguageItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.language == b.language }
)