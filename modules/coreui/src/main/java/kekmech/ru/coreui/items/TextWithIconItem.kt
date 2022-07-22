package kekmech.ru.coreui.items

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemTextWithIconBinding

private const val DEFAULT_TEXT_WITH_ICON_ITEM_ID = 0

data class TextWithIconItem(
    val itemId: Int = DEFAULT_TEXT_WITH_ICON_ITEM_ID,
    val text: CharSequence? = null,
    @StringRes val textResId: Int = 0,
    @DrawableRes val drawableResID: Int = 0,
    @AttrRes val tintColorAttrId: Int? = null,
    @StyleRes val textStyleResId: Int? = null
)

interface TextWithIconViewHolder : ClickableItemViewHolder {
    fun setText(text: CharSequence)
    fun setText(@StringRes textResId: Int)
    fun setIcon(@DrawableRes drawableResID: Int)
    fun setTintColor(@AttrRes tintColorAttrId: Int)
    fun setTextStyle(@StyleRes styleResId: Int)
}

private class TextWithIconViewHolderImpl(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    TextWithIconViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(itemView) {

    private val viewBinding = ItemTextWithIconBinding.bind(itemView)

    override fun setIcon(drawableResID: Int) {
        viewBinding.imageViewIcon.setImageResource(drawableResID)
    }

    override fun setText(text: CharSequence) {
        viewBinding.textViewMainText.text = text
    }

    override fun setText(textResId: Int) {
        viewBinding.textViewMainText.setText(textResId)
    }

    override fun setTintColor(tintColorAttrId: Int) {
        with(viewBinding) {
            val color = root.context.getThemeColor(tintColorAttrId)
            imageViewIcon.imageTintList = ColorStateList.valueOf(color)
            textViewMainText.setTextColor(color)
        }
    }

    override fun setTextStyle(styleResId: Int) {
        with(viewBinding) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                textViewMainText.setTextAppearance(styleResId)
            } else {
                @Suppress("DEPRECATION")
                textViewMainText.setTextAppearance(textViewMainText.context, styleResId)
            }
        }
    }
}

class TextWithIconAdapterItem(
    onCLickListener: (TextWithIconItem) -> Unit
) : AdapterItem<TextWithIconViewHolder, TextWithIconItem>(
    isType = { it is TextWithIconItem },
    layoutRes = R.layout.item_text_with_icon,
    viewHolderGenerator = ::TextWithIconViewHolderImpl,
    itemBinder = object : BaseItemBinder<TextWithIconViewHolder, TextWithIconItem>() {
        override fun bind(vh: TextWithIconViewHolder, model: TextWithIconItem, position: Int) {
            vh.setIcon(model.drawableResID)
            model.text?.let(vh::setText) ?: vh.setText(model.textResId)
            model.textStyleResId?.let(vh::setTextStyle)
            model.tintColorAttrId?.let(vh::setTintColor)
            vh.setOnClickListener { onCLickListener(model) }
        }
    },
    areItemsTheSame = { a, b -> a.itemId == b.itemId }
)
