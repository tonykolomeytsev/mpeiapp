package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_text.*

data class TextItem(
    val text: CharSequence? = null,
    @StringRes val textResId: Int? = null,
    @StyleRes val styleResId: Int? = null
)

interface TextItemViewHolder {
    fun setText(text: CharSequence)
    fun setText(@StringRes textResId: Int)
    fun setStyle(@StyleRes styleResId: Int)
}

class TextItemViewHolderImpl(
    override val containerView: View
) : TextItemViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun setText(text: CharSequence) {
        textViewTextItem.text = text
    }

    override fun setText(@StringRes textResId: Int) {
        textViewTextItem.setText(textResId)
    }

    override fun setStyle(@StyleRes styleResId: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            textViewTextItem.setTextAppearance(styleResId)
        } else {
            @Suppress("DEPRECATION")
            textViewTextItem.setTextAppearance(containerView.context, styleResId)
        }
    }
}

class TextItemBinder : BaseItemBinder<TextItemViewHolder, TextItem>() {

    override fun bind(vh: TextItemViewHolder, model: TextItem, position: Int) {
        model.apply {
            text?.let(vh::setText)
            textResId?.let(vh::setText)
            styleResId?.let(vh::setStyle)
        }
    }
}

class TextAdapterItem(
    @LayoutRes layoutRes: Int = R.layout.item_text
) : AdapterItem<TextItemViewHolder, TextItem>(
    isType = { it is TextItem },
    layoutRes = layoutRes,
    viewHolderGenerator = ::TextItemViewHolderImpl,
    itemBinder = TextItemBinder()
)